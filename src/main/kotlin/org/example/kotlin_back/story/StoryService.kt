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

    fun generateStory(symptome: String, salle: String, etat: String): String {
        val restTemplate = RestTemplate()
        val requestBody = mapOf("symptome" to symptome, "salle" to salle, "etat" to etat)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val requestEntity = HttpEntity(requestBody, headers)

        return try {
            log.info("-> Calling IA API at URL: {}", iaApiUrl)
            val response = restTemplate.postForEntity<Map<*, *>>(iaApiUrl, requestEntity)
            val texte = response.body?.get("texte") as? String
            log.info("<- Successfully received story from IA API.")

            texte ?: "Erreur : le texte généré est manquant ou invalide dans la réponse."
        } catch (e: Exception) {
            log.error("!!! FAILED to communicate with IA API at {}: {}", iaApiUrl, e.message)
            "Erreur de communication avec le générateur d’histoire IA."
        }
    }
}