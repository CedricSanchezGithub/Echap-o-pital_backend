package org.example.kotlin_back.story

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/story")
class StoryController(
    private val storyService: StoryService
) {
    companion object {
        private val log = LoggerFactory.getLogger(StoryController::class.java)
    }

    @PostMapping("/generate")
    fun generateStory(@RequestBody request: StoryRequest): Map<String, String> {
        log.info("-> Received request to generate story for room: {}", request.salle)
        val storyText = storyService.generateStory(
            symptome = request.symptome,
            salle = request.salle,
            etat = request.etat
        )
        return mapOf("texte" to storyText)
    }
}