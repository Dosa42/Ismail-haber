package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ContentCategory
import com.example.data.repository.ContentRepository
import com.example.ui.MainViewModel
import com.example.ui.components.AudioEqualizerAnimation
import com.example.ui.components.WhatsAppShareButton
import com.example.ui.theme.FlagRed
import com.example.ui.theme.ReisAmber
import com.example.ui.theme.YuzyilNavy

@Composable
fun DefenseScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isElderMode by viewModel.isElderTextMode.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking
    val currentAudioId by viewModel.currentPlayingAudioId.collectAsState()

    val defenseItems = remember {
        ContentRepository.staticContentList.filter {
            it.category == ContentCategory.SAVUNMA || it.category == ContentCategory.TEKNOLOJI
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("defense_screen_feed"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Image & Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_savunma_sanayii),
                    contentDescription = "Milli Savunma Sanayii",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    YuzyilNavy.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        color = FlagRed,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "MİLLİ TEKNOLOJİ HAMLESİ",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Gök Vatan & Mavi Vatan Muhafızları",
                        color = Color.White,
                        fontSize = if (isElderMode) 22.sp else 19.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "Yerlilik Oranı: %80+ • Tam Bağımsız Türkiye",
                        color = ReisAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Project Items
        items(defenseItems, key = { "def_${it.id}" }) { item ->
            val isPlaying = isSpeaking && currentAudioId == item.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { viewModel.openDetail(item) }
                    .testTag("defense_card_${item.id}"),
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
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = item.source,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                            text = "⚡ « ${item.quote} »",
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
                        category = "Milli Savunma",
                        fullWidth = true
                    )
                }
            }
        }
    }
}
