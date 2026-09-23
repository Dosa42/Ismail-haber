package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ContentRepository
import com.example.ui.MainViewModel
import com.example.ui.components.AudioEqualizerAnimation
import com.example.ui.components.WhatsAppShareButton
import com.example.ui.theme.FlagRed
import com.example.ui.theme.ReisAmber
import com.example.ui.theme.ReisGold
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.YuzyilNavy
import com.example.util.ShareHelper

@Composable
fun RaconScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isElderMode by viewModel.isElderTextMode.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking
    val currentAudioId by viewModel.currentPlayingAudioId.collectAsState()
    val context = LocalContext.current

    val raconList = remember {
        ContentRepository.staticContentList.filter { it.quote != null }
    }

    var customGeneratedRacon by remember { mutableStateOf<String?>(null) }
    var selectedMood by remember { mutableStateOf("Kahve Grubu") }

    val presetRacons = listOf(
        "⚡ 'Bu millete parmak sallayanlar, tarihin çöplüğüne gömülecektir!'",
        "🦅 'Biz bu yola kefenimizi giyip çıktık, geri dönüş yok!'",
        "🇹🇷 'Topunuz gelin! Ne bu bayrak iner, ne bu vatan bölünür!'",
        "☕ 'Kahvedeki tüm dostlara ve dava arkadaşlarına Reis'in selamı var! Dik durun, memleket emin ellerde!'"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("racon_screen_feed"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                FlagRed,
                                Color(0xFF5A0000),
                                YuzyilNavy
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ReisAmber,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "DİK DUR EĞİLME",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = ReisAmber,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Reis'in Efsane Sözleri & Racon Capsleri",
                        color = Color.White,
                        fontSize = if (isElderMode) 24.sp else 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tek tuşla dinleyin, WhatsApp'tan ailenize, arkadaşlarınıza veya gruplara hemen gönderin!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = if (isElderMode) 16.sp else 13.sp
                    )
                }
            }
        }

        // Quick WhatsApp Generator Section for Father
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("racon_quick_generator"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ReisAmber,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Babaya Özel: Hazır WhatsApp Racon Mesajı",
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isElderMode) 17.sp else 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Kime göndermek istersiniz?",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Kahve Grubu", "Aile Grubu", "Dostlar").forEach { mood ->
                            val isSel = selectedMood == mood
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedMood = mood
                                        customGeneratedRacon = when (mood) {
                                            "Kahve Grubu" -> presetRacons[3]
                                            "Aile Grubu" -> "🇹🇷 Kıymetli ailem; Reis'in izinde, vatanımızın emrinde hayırlı günler dilerim! Dik dur eğilme, bu millet seninle!"
                                            else -> presetRacons[0]
                                        }
                                    }
                            ) {
                                Text(
                                    text = mood,
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val activeText = customGeneratedRacon ?: presetRacons[3]
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = activeText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = if (isElderMode) 16.sp else 13.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    WhatsAppShareButton(
                        title = "Reis'ten Selam & Racon Mesajı",
                        summary = activeText,
                        quote = activeText,
                        category = "Racon & Lider",
                        fullWidth = true,
                        text = "WhatsApp ile Gönder"
                    )
                }
            }
        }

        // List of Authentic Racon Quotes
        items(raconList, key = { "racon_${it.id}" }) { item ->
            val quote = item.quote ?: item.title
            val isPlaying = isSpeaking && currentAudioId == "quote_${item.id}"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("racon_card_${item.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    FlagRed.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = FlagRed
                            ) {
                                Text(
                                    text = item.raconAudioTitle ?: "REİS RACON",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isPlaying) {
                                    AudioEqualizerAnimation(isPlaying = true)
                                    Spacer(modifier = Modifier.width(6.dp))
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.speakText("Reis diyor ki: $quote", "quote_${item.id}")
                                    },
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                                        contentDescription = "Sesli Dinle",
                                        tint = if (isPlaying) FlagRed else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = ReisAmber,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "« $quote »",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isElderMode) 22.sp else 18.sp,
                                    lineHeight = if (isElderMode) 30.sp else 25.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WhatsAppShareButton(
                            title = item.title,
                            summary = item.summary,
                            quote = quote,
                            category = "Reis Racon",
                            fullWidth = true
                        )
                    }
                }
            }
        }
    }
}
