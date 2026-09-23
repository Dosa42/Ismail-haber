package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentCategory
import com.example.data.model.ContentItem
import com.example.ui.theme.FlagRed
import com.example.ui.theme.ReisAmber
import com.example.ui.theme.ReisGold
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenDark
import com.example.ui.theme.YuzyilNavy
import com.example.util.ShareHelper

@Composable
fun WhatsAppShareButton(
    title: String,
    summary: String,
    quote: String? = null,
    category: String? = null,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
    text: String = "WhatsApp'ta Paylaş"
) {
    val context = LocalContext.current
    Button(
        onClick = {
            ShareHelper.shareViaWhatsApp(
                context = context,
                title = title,
                summary = summary,
                quote = quote,
                category = category
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = WhatsAppGreen,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        modifier = modifier
            .testTag("whatsapp_share_button")
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
    ) {
        Icon(
            imageVector = Icons.Default.Whatsapp,
            contentDescription = "WhatsApp Paylaş",
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: ContentCategory,
    onCategorySelected: (ContentCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.testTag("category_filter_row"),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ContentCategory.values()) { cat ->
            val isSelected = cat == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(cat) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(cat.iconEmoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(cat.displayName, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("filter_chip_${cat.name}")
            )
        }
    }
}

@Composable
fun RaconQuoteCard(
    quote: String,
    title: String,
    categoryName: String,
    onSpeakClick: () -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("racon_quote_card"),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            FlagRed.copy(alpha = 0.12f),
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
                        color = FlagRed,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⚡ REİS'İN TARİHİ SÖZÜ",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Row {
                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier.size(36.dp).testTag("quote_tts_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Pause else Icons.Default.VolumeUp,
                                contentDescription = if (isSpeaking) "Durdur" else "Seslendir",
                                tint = if (isSpeaking) FlagRed else MaterialTheme.colorScheme.primary
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
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = quote,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WhatsAppShareButton(
                        title = "Reis'ten Tarihi Söz",
                        summary = quote,
                        quote = quote,
                        category = categoryName,
                        fullWidth = true
                    )
                }
            }
        }
    }
}

@Composable
fun AudioEqualizerAnimation(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val transition = rememberInfiniteTransition(label = "eq")
        val h1 by transition.animateFloat(
            initialValue = 4f,
            targetValue = if (isPlaying) 20f else 6f,
            animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
            label = "h1"
        )
        val h2 by transition.animateFloat(
            initialValue = 8f,
            targetValue = if (isPlaying) 24f else 8f,
            animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
            label = "h2"
        )
        val h3 by transition.animateFloat(
            initialValue = 6f,
            targetValue = if (isPlaying) 18f else 5f,
            animationSpec = infiniteRepeatable(tween(310), RepeatMode.Reverse),
            label = "h3"
        )
        val h4 by transition.animateFloat(
            initialValue = 10f,
            targetValue = if (isPlaying) 26f else 9f,
            animationSpec = infiniteRepeatable(tween(480), RepeatMode.Reverse),
            label = "h4"
        )

        Box(modifier = Modifier.width(3.dp).height(h1.dp).clip(CircleShape).background(FlagRed))
        Box(modifier = Modifier.width(3.dp).height(h2.dp).clip(CircleShape).background(ReisAmber))
        Box(modifier = Modifier.width(3.dp).height(h3.dp).clip(CircleShape).background(FlagRed))
        Box(modifier = Modifier.width(3.dp).height(h4.dp).clip(CircleShape).background(ReisAmber))
    }
}
