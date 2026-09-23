package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ContentCategory
import com.example.data.model.ContentItem
import com.example.data.remote.GeminiService
import com.example.data.repository.ContentRepository
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val sender: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ContentRepository(database.savedItemDao())
    val ttsManager = TtsManager(application)

    private val _selectedCategory = MutableStateFlow(ContentCategory.ALL)
    val selectedCategory: StateFlow<ContentCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isElderTextMode = MutableStateFlow(false)
    val isElderTextMode: StateFlow<Boolean> = _isElderTextMode.asStateFlow()

    private val _activeDetailItem = MutableStateFlow<ContentItem?>(null)
    val activeDetailItem: StateFlow<ContentItem?> = _activeDetailItem.asStateFlow()

    private val _currentPlayingAudioId = MutableStateFlow<String?>(null)
    val currentPlayingAudioId: StateFlow<String?> = _currentPlayingAudioId.asStateFlow()

    val savedItems: StateFlow<List<ContentItem>> = repository.getSavedItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Reis AI Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "model",
                text = "Hürmetler kıymetli babacığım! Hoş geldiniz. Ben Türkiye Yüzyılı milli rehberinizim. Siyaset, savunma sanayii, KAAN, TOGG, Reisimizin icraatları veya şanlı ecdadımız hakkında dilediğinizi sorabilirsiniz. Emrinizdeyim!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun selectCategory(category: ContentCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleElderTextMode() {
        _isElderTextMode.value = !_isElderTextMode.value
    }

    fun openDetail(item: ContentItem) {
        _activeDetailItem.value = item
    }

    fun closeDetail() {
        _activeDetailItem.value = null
    }

    fun isItemSaved(id: String): Boolean {
        return savedItems.value.any { it.id == id }
    }

    fun toggleSave(item: ContentItem) {
        viewModelScope.launch {
            if (isItemSaved(item.id)) {
                repository.removeItem(item.id)
            } else {
                repository.saveItem(item)
            }
        }
    }

    fun toggleSpeech(item: ContentItem) {
        if (ttsManager.isSpeaking.value && ttsManager.currentUtteranceId.value == item.id) {
            ttsManager.stop()
            _currentPlayingAudioId.value = null
        } else {
            val textToRead = "${item.title}. ${item.summary} ${if (item.quote != null) "Reis'in sözü: ${item.quote}" else ""}"
            _currentPlayingAudioId.value = item.id
            ttsManager.speak(textToRead, item.id)
        }
    }

    fun speakText(text: String, id: String) {
        if (ttsManager.isSpeaking.value && ttsManager.currentUtteranceId.value == id) {
            ttsManager.stop()
            _currentPlayingAudioId.value = null
        } else {
            _currentPlayingAudioId.value = id
            ttsManager.speak(text, id)
        }
    }

    fun sendChatMessage(prompt: String) {
        if (prompt.isBlank() || _isChatLoading.value) return

        val userMsg = ChatMessage(sender = "user", text = prompt)
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val history = _chatMessages.value.map { it.sender to it.text }
            val result = GeminiService.askReisAi(history, prompt)
            _isChatLoading.value = false
            result.onSuccess { reply ->
                _chatMessages.value = _chatMessages.value + ChatMessage(sender = "model", text = reply)
            }.onFailure { err ->
                val fallbackReply = when {
                    prompt.contains("kaan", ignoreCase = true) ->
                        "Kıymetli büyüğüm, KAAN 5. nesil milli muharip uçağımızdır. TUSAŞ tarafından üretilmiş olup gök vatandaki bağımsızlığımızın çelik kanadıdır! Çift motorlu, radara görünmez ve yerli mühimmatlarımızla donatılmıştır."
                    prompt.contains("togg", ignoreCase = true) ->
                        "Saygıdeğer babamız, TOGG Gemlik tesislerimizde üretilen yerli ve milli akıllı cihazımızdır. Yollarda yüz bini aşkın vatandaşımız gururla kullanıyor, Avrupa'ya da ihraç ediyoruz."
                    prompt.contains("reis", ignoreCase = true) || prompt.contains("erdoğan", ignoreCase = true) ->
                        "Reisimiz Recep Tayyip Erdoğan, 'Biz bu millete efendi olmaya değil, hizmetkâr olmaya geldik' diyerek gece gündüz vatanımız için çalışmaya devam ediyor. Dik duruşundan asla taviz vermiyor!"
                    else ->
                        "Hürmetler babacığım! Türkiye Yüzyılı yolunda milli hedeflerimize kararlılıkla yürüyoruz. Savunmada %80'in üzerinde yerlilik oranına ulaştık, memleketimizin her köşesinde kalkınma hamleleri sürüyor."
                }
                _chatMessages.value = _chatMessages.value + ChatMessage(sender = "model", text = fallbackReply)
            }
        }
    }

    fun getFilteredContentList(): List<ContentItem> {
        val cat = _selectedCategory.value
        val query = _searchQuery.value.trim().lowercase()

        val list = repository.getContentsByCategory(cat)
        if (query.isEmpty()) return list

        return list.filter {
            it.title.lowercase().contains(query) ||
            it.summary.lowercase().contains(query) ||
            (it.quote != null && it.quote.lowercase().contains(query)) ||
            it.tags.any { tag -> tag.lowercase().contains(query) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.release()
    }
}
