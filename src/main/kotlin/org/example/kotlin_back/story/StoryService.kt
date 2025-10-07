package org.example.kotlin_back.story

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.util.logging.Logger

@Service
class StoryService(
    @Value("\${ia.api.url}") private val iaApiUrl: String
) {
    companion object {
        private val LOG = Logger.getLogger(StoryService::class.java.name)
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
            val response = restTemplate.postForEntity<Map<*, *>>(iaApiUrl, requestEntity)
            val body = response.body
            val texte = body?.get("texte") as? String

            texte ?: "Erreur : le texte généré est manquant ou invalide dans la réponse."
        } catch (e: Exception) {
            LOG.severe("Erreur de communication avec le générateur d'histoire IA à l'URL $iaApiUrl: ${e.message}")
            "Erreur de communication avec le générateur d’histoire IA."
        }
    }
}