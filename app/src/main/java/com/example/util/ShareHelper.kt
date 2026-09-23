package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

object ShareHelper {

    fun formatShareText(
        title: String,
        summary: String,
        quote: String? = null,
        category: String? = null
    ): String {
        val sb = StringBuilder()
        sb.append("🇹🇷 *TÜRKİYE YÜZYILI* 🇹🇷\n")
        if (!category.isNullOrBlank()) {
            sb.append("🏷️ *[$category]*\n\n")
        }
        sb.append("📌 *").append(title.trim()).append("*\n\n")
        sb.append(summary.trim()).append("\n\n")

        if (!quote.isNullOrBlank()) {
            sb.append("💬 *Reis'ten Tarihi Söz:*\n")
            sb.append("« ").append(quote.trim()).append(" »\n\n")
        }

        sb.append("⚡ _\"Dik dur eğilme, bu aziz millet seninle!\"_\n")
        sb.append("📲 Türkiye Yüzyılı Özel Uygulaması ile Paylaşıldı.")
        return sb.toString()
    }

    fun shareViaWhatsApp(
        context: Context,
        title: String,
        summary: String,
        quote: String? = null,
        category: String? = null
    ) {
        val message = formatShareText(title, summary, quote, category)
        val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            context.startActivity(whatsappIntent)
        } catch (e: Exception) {
            // WhatsApp is not installed or available on this device; fall back to general chooser
            val chooserIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            try {
                context.startActivity(
                    Intent.createChooser(chooserIntent, "WhatsApp veya Başka Uygulama ile Paylaş")
                )
            } catch (err: Exception) {
                copyToClipboard(context, message)
                Toast.makeText(context, "Metin panoya kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Türkiye Yüzyılı", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Metin kopyalandı, dilediğiniz yere yapıştırabilirsiniz!", Toast.LENGTH_SHORT).show()
    }
}
