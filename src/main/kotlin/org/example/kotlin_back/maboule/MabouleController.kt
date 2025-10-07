package org.example.kotlin_back.maboule

import org.example.kotlin_back.entity.GameResponse
import org.example.kotlin_back.game.GameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api")
class MabouleController(
    private val mabouleSservice: MabouleService,
    private val gameService: GameService
) {

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGame(): GameResponse {
        return gameService.createNewGame()
    }

    @PostMapping("/games/{gameId}/errors")
    @ResponseStatus(HttpStatus.CREATED)
    fun addError(@PathVariable gameId: String) {
        gameService.recordError(gameId)
    }

    @GetMapping("/games/{gameId}/events")
    fun subscribeToGameEvents(@PathVariable gameId: String): SseEmitter {
        return mabouleSservice.createEmitter(gameId)
    }
}