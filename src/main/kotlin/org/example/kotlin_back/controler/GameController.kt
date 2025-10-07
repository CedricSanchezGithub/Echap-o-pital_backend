package org.example.kotlin_back.controler

import org.example.kotlin_back.service.GameService
import org.example.kotlin_back.entity.GameResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class GameController(private val gameService: GameService) {

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
}