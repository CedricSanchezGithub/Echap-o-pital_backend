package org.example.kotlin_back.story

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Service
class StoryService(
    @Value("\${ia.api.url}") private val iaApiUrl: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(StoryService::class.java)
    }

    fun generateStory(symptome: String, salle: String, etat: String): Map<String, Any> {
        val restTemplate = RestTemplate()
        val requestBody = mapOf("symptome" to symptome, "salle" to salle, "etat" to etat)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val requestEntity = HttpEntity(requestBody, headers)

        return try {
            log.info("-> Calling IA API at URL: {}", iaApiUrl)
            val response = restTemplate.postForEntity<Map<String, Any>>(iaApiUrl, requestEntity)
            log.info("<- Successfully received story from IA API.")

            response.body ?: mapOf(
                "dialogue" to "Erreur : le dialogue généré est manquant.",
                "choix" to listOf(mapOf("id" to "continuer", "texte" to "Continuer..."))
            )
        } catch (e: Exception) {
            log.error("!!! FAILED to communicate with IA API at {}: {}", iaApiUrl, e.message)
            mapOf(
                "dialogue" to "Erreur de communication avec le générateur d’histoire IA.",
                "choix" to listOf(mapOf("id" to "continuer", "texte" to "Réessayer"))
            )
        }
    }
}