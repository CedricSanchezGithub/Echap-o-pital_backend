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

    fun generateOrContinueStory(request: StoryRequest): Map<String, Any> {
        val restTemplate = RestTemplate()

        // On choisit le corps de la requête en fonction de la présence de choice_id
        val requestBody = if (request.choiceId != null) {
            log.info("-> Continuing story with choice_id: {}", request.choiceId)
            mapOf("choice_id" to request.choiceId)
        } else {
            log.info("-> Generating new story for room: {}", request.salle)
            mapOf("symptome" to request.symptome, "salle" to request.salle, "etat" to request.etat)
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val requestEntity = HttpEntity(requestBody, headers)

        return try {
            log.info("-> Calling IA API at URL: {}", iaApiUrl)
            val response = restTemplate.postForEntity<Map<String, Any>>(iaApiUrl, requestEntity)
            log.info("<- Successfully received response from IA API.")

            response.body ?: errorResponse("La réponse de l'IA est vide.")
        } catch (e: Exception) {
            log.error("!!! FAILED to communicate with IA API at {}: {}", iaApiUrl, e.message)
            errorResponse("Erreur de communication avec le générateur d’histoire IA.")
        }
    }

    private fun errorResponse(message: String): Map<String, Any> {
        return mapOf(
            "dialogue" to message,
            "choix" to listOf(mapOf("id" to "continuer", "texte" to "Continuer..."))
        )
    }
}