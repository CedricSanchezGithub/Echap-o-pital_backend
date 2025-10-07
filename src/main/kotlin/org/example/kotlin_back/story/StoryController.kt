package org.example.kotlin_back.story

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/story")
class StoryController(

    private val storyService: StoryService
) {

    @PostMapping("/generate")
    fun generateStory(@RequestBody request: StoryRequest): String {
        return storyService.generateStory(request.symptome, request.salle, request.etat)
    }
}