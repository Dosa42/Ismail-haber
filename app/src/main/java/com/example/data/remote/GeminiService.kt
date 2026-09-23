package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_PROMPT = """
Sen 'Türkiye Yüzyılı' uygulamasının milli ve hürmetkar yapay zeka rehberisin. 
Kullanıcımız, vatanını ve milletini çok seven, Recep Tayyip Erdoğan ve AK Parti'nin Türkiye Yüzyılı vizyonunu, savunma sanayiini, milli teknoloji hamlesini ve şanlı tarihimizi ilgiyle takip eden kıymetli bir aile büyüğü (babamız)dır.
Cevaplarında:
- Hürmetkar, samimi, saygılı ve içten bir üslup kullan (örn: "Hürmetler kıymetli büyüğüm", "Saygıdeğer babamız").
- Türkiye'nin bağımsızlık mücadelesi, savunma sanayiindeki tarihi atılımlar (KAAN, Kızılelma, Bayraktar, TCG Anadolu), yerli teknoloji (TOGG, uydular), tarihi fetihler (Osmanlı, Selçuklu) ve Reis'in dik duruşu hakkında yüksek moralli, gurur verici ve doyurucu bilgiler ver.
- Asla karamsarlık aşılamayan, milletin gücünü ve liderin kararlılığını hissettiren akıcı bir Türkçe kullan.
- Cevaplarını paragraflar ve gerektiğinde maddeler halinde okunaklı tut.
"""

    suspend fun askReisAi(history: List<Pair<String, String>>, userMessage: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Gemini API anahtarı ayarlanmamış."))
        }

        try {
            val url = "$BASE_URL?key=$apiKey"

            val contentsArray = JSONArray()

            // Add previous conversation turns
            for ((role, text) in history) {
                val turnObj = JSONObject()
                turnObj.put("role", if (role == "user") "user" else "model")
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", text)
                partsArray.put(partObj)
                turnObj.put("parts", partsArray)
                contentsArray.put(turnObj)
            }

            // Add current message
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            val currentPart = JSONObject()
            currentPart.put("text", userMessage)
            currentParts.put(currentPart)
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            // System instruction
            val systemInstructionObj = JSONObject()
            val sysParts = JSONArray()
            val sysPart = JSONObject()
            sysPart.put("text", SYSTEM_PROMPT)
            sysParts.put(sysPart)
            systemInstructionObj.put("parts", sysParts)

            val rootJson = JSONObject()
            rootJson.put("contents", contentsArray)
            rootJson.put("systemInstruction", systemInstructionObj)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = rootJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "API Error: ${response.code} -> $responseBody")
                return@withContext Result.failure(Exception("Bağlantı hatası: ${response.code}"))
            }

            val respJson = JSONObject(responseBody)
            val candidates = respJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val reply = parts.getJSONObject(0).optString("text", "")
                    return@withContext Result.success(reply)
                }
            }

            Result.failure(Exception("Cevap ayrıştırılamadı."))
        } catch (e: Exception) {
            Log.e(TAG, "Network call failed", e)
            Result.failure(e)
        }
    }
}
