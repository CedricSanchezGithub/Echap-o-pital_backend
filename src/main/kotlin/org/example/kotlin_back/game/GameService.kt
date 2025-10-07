package org.example.kotlin_back.game

import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository
) {
    fun createNewGame(): GameResponse {
        val newGame = GameEntity.new()
        val savedGame = gameRepository.save(newGame)
        return GameResponse(gameId = savedGame.id)
    }
}