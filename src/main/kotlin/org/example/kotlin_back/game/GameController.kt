package org.example.kotlin_back.game

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGame(): GameResponse {
        return gameService.createNewGame()
    }
}