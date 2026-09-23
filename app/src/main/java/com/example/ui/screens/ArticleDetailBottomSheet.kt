package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ContentItem
import com.example.ui.MainViewModel
import com.example.ui.components.AudioEqualizerAnimation
import com.example.ui.components.WhatsAppShareButton
import com.example.ui.theme.FlagRed
import com.example.ui.theme.ReisAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailBottomSheet(
    item: ContentItem,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isElderMode by viewModel.isElderTextMode.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking
    val currentAudioId by viewModel.currentPlayingAudioId.collectAsState()
    val isPlayingThis = isSpeaking && currentAudioId == item.id
    val isSaved = viewModel.isItemSaved(item.id)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("article_detail_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Top Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.category.displayName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isPlayingThis) {
                        AudioEqualizerAnimation(isPlaying = true)
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Listen Aloud
                    IconButton(
                        onClick = { viewModel.toggleSpeech(item) },
                        modifier = Modifier.size(40.dp).testTag("detail_speech_button")
                    ) {
                        Icon(
                            imageVector = if (isPlayingThis) Icons.Default.Pause else Icons.Default.VolumeUp,
                            contentDescription = "Sesli Oku",
                            tint = if (isPlayingThis) FlagRed else MaterialTheme.colorScheme.primary
                        )
                    }

                    // Font Size Toggle (Baba Modu)
                    IconButton(
                        onClick = { viewModel.toggleElderTextMode() },
                        modifier = Modifier.size(40.dp).testTag("detail_font_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Yazı Boyutu",
                            tint = if (isElderMode) FlagRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Bookmark
                    IconButton(
                        onClick = { viewModel.toggleSave(item) },
                        modifier = Modifier.size(40.dp).testTag("detail_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Kaydet",
                            tint = if (isSaved) ReisAmber else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Close
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp).testTag("detail_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Image Banner
            if (item.imageResName == "banner_savunma_sanayii") {
                Image(
                    painter = painterResource(id = R.drawable.banner_savunma_sanayii),
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (isElderMode) 25.sp else 21.sp,
                        lineHeight = if (isElderMode) 33.sp else 28.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "📅 ${item.date}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "🏛️ ${item.source}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quote Card
                if (!item.quote.isNullOrBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ReisAmber.copy(alpha = 0.12f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ReisAmber.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = ReisAmber,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "« ${item.quote} »",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isElderMode) 18.sp else 15.sp,
                                    lineHeight = if (isElderMode) 26.sp else 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Full Body Text
                Text(
                    text = item.fullBody,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (isElderMode) 20.sp else 16.sp,
                        lineHeight = if (isElderMode) 30.sp else 25.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Prominent WhatsApp Share Button
                WhatsAppShareButton(
                    title = item.title,
                    summary = item.summary,
                    quote = item.quote,
                    category = item.category.displayName,
                    fullWidth = true,
                    text = "Bu İçeriği WhatsApp ile Gönder"
                )
            }
        }
    }
}
