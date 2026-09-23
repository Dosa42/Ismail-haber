package com.example.data.model

enum class ContentCategory(val displayName: String, val iconEmoji: String) {
    ALL("Tümü", "🇹🇷"),
    POLITIKA("Siyaset & Lider", "🏛️"),
    SAVUNMA("Milli Savunma", "🦅"),
    TEKNOLOJI("Teknoloji & Projeler", "🚀"),
    RACON("Racon & Sözler", "⚡"),
    TARIH("Tarih & Ecdad", "📜"),
    EKONOMI("Ekonomi & Müjdeler", "💎")
}

data class ContentItem(
    val id: String,
    val title: String,
    val category: ContentCategory,
    val summary: String,
    val fullBody: String,
    val quote: String? = null,
    val date: String,
    val source: String = "Milli Haber Merkezi",
    val readTimeMinutes: Int = 3,
    val imageResName: String? = null,
    val raconAudioTitle: String? = null,
    val tags: List<String> = emptyList(),
    val isBreaking: Boolean = false
)
