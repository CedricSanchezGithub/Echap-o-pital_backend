package org.example.kotlin_back.maboule

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/games/{gameId}/maboule")
class MabouleController(
    private val mabouleService: MabouleService
) {
    @PostMapping("/errors")
    @ResponseStatus(HttpStatus.CREATED)
    fun addError(@PathVariable gameId: String) {
        mabouleService.recordError(gameId)
    }

    @GetMapping("/events")
    fun subscribeToGameEvents(@PathVariable gameId: String): SseEmitter {
        return mabouleService.subscribeToGameEvents(gameId)
    }
}