package org.example.kotlin_back.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.util.logging.Logger

@Service
class StoryGeneratorService {
    companion object {
        private const val IA_API_URL = "http://localhost:5000/generate"
        private val LOG = Logger.getLogger(StoryGeneratorService::class.java.name)
    }

    fun generateStory(symptome: String, salle: String, etat: String): String {
        val restTemplate = RestTemplate()

        val requestBody = mapOf(
            "symptome" to symptome,
            "salle" to salle,
            "etat" to etat
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val requestEntity = HttpEntity(requestBody, headers)

        return try {
            val response = restTemplate.postForEntity<Map<*, *>>(IA_API_URL, requestEntity)
            val body = response.body
            val texte = body?.get("texte") as? String // Safe cast

            texte ?: "Erreur : le texte généré est manquant ou invalide dans la réponse."

        } catch (e: Exception) {
            LOG.severe("Erreur de communication avec le générateur d'histoire IA: ${e.message}")
            "Erreur de communication avec le générateur d’histoire IA."
        }
    }
}