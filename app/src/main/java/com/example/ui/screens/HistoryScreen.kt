package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentCategory
import com.example.data.repository.ContentRepository
import com.example.ui.MainViewModel
import com.example.ui.components.AudioEqualizerAnimation
import com.example.ui.components.WhatsAppShareButton
import com.example.ui.theme.FlagRed
import com.example.ui.theme.ReisAmber
import com.example.ui.theme.YuzyilNavy

@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isElderMode by viewModel.isElderTextMode.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking
    val currentAudioId by viewModel.currentPlayingAudioId.collectAsState()

    val historyItems = remember {
        ContentRepository.staticContentList.filter {
            it.category == ContentCategory.TARIH
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("history_screen_feed"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header
        item {
            Surface(
                color = YuzyilNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        color = ReisAmber,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "KUTLU MEDENİYET MİRASI",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tarih ve Şanlı Ecdadımız",
                        color = Color.White,
                        fontSize = if (isElderMode) 24.sp else 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Fatih Sultan Mehmet Han'dan Abdülhamid Han'a, Malazgirt'ten Çanakkale'ye şanlı Türk tarihi.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = if (isElderMode) 16.sp else 13.sp
                    )
                }
            }
        }

        items(historyItems, key = { "hist_${it.id}" }) { item ->
            val isPlaying = isSpeaking && currentAudioId == item.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { viewModel.openDetail(item) }
                    .testTag("history_card_${item.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ReisAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "📜 ${item.source}",
                                color = ReisAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isPlaying) {
                                AudioEqualizerAnimation(isPlaying = true)
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            IconButton(
                                onClick = { viewModel.toggleSpeech(item) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                                    contentDescription = "Sesli Oku",
                                    tint = if (isPlaying) FlagRed else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isElderMode) 20.sp else 16.sp,
                            lineHeight = if (isElderMode) 26.sp else 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = item.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = if (isElderMode) 16.sp else 14.sp,
                            lineHeight = if (isElderMode) 23.sp else 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!item.quote.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "« ${item.quote} »",
                            fontSize = if (isElderMode) 15.sp else 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FlagRed
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    WhatsAppShareButton(
                        title = item.title,
                        summary = item.summary,
                        quote = item.quote,
                        category = "Tarih ve Ecdad",
                        fullWidth = true
                    )
                }
            }
        }
    }
}
