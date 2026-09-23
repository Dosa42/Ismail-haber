package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ContentCategory
import com.example.data.model.ContentItem

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val categoryName: String,
    val summary: String,
    val fullBody: String,
    val quote: String?,
    val date: String,
    val source: String,
    val readTimeMinutes: Int,
    val imageResName: String?,
    val raconAudioTitle: String?,
    val savedAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toContentItem(): ContentItem {
        val cat = try {
            ContentCategory.valueOf(categoryName)
        } catch (e: Exception) {
            ContentCategory.POLITIKA
        }
        return ContentItem(
            id = id,
            title = title,
            category = cat,
            summary = summary,
            fullBody = fullBody,
            quote = quote,
            date = date,
            source = source,
            readTimeMinutes = readTimeMinutes,
            imageResName = imageResName,
            raconAudioTitle = raconAudioTitle
        )
    }

    companion object {
        fun fromContentItem(item: ContentItem): SavedItemEntity {
            return SavedItemEntity(
                id = item.id,
                title = item.title,
                categoryName = item.category.name,
                summary = item.summary,
                fullBody = item.fullBody,
                quote = item.quote,
                date = item.date,
                source = item.source,
                readTimeMinutes = item.readTimeMinutes,
                imageResName = item.imageResName,
                raconAudioTitle = item.raconAudioTitle
            )
        }
    }
}
