package org.example.kotlin_back.controler

import org.example.kotlin_back.entity.StoryRequest
import org.example.kotlin_back.service.StoryGeneratorService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/story")
class StoryController(

    private val storyService: StoryGeneratorService
) {

    @PostMapping("/generate")
    fun generateStory(@RequestBody request: StoryRequest): String {
        return storyService.generateStory(request.symptome, request.salle, request.etat)
    }
}