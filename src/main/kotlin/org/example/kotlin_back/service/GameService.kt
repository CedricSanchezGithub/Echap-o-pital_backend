package org.example.kotlin_back.service

import org.example.kotlin_back.entity.GameAlreadyFinishedException
import org.example.kotlin_back.entity.GameEntity
import org.example.kotlin_back.entity.GameError
import org.example.kotlin_back.entity.GameNotFoundException
import org.example.kotlin_back.entity.GameResponse
import org.example.kotlin_back.entity.GameStatus
import org.example.kotlin_back.repository.GameErrorRepository
import org.example.kotlin_back.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gameErrorRepository: GameErrorRepository
) {
    fun createNewGame(): GameResponse {
        val newGame = GameEntity.new()
        val savedGame = gameRepository.save(newGame)
        return GameResponse(gameId = savedGame.id)
    }
    fun recordError(gameId: String) {
        // 1. Trouve la partie ou lève une exception 404
        val game = gameRepository.findById(gameId)
            .orElseThrow { GameNotFoundException("Game not found with id: $gameId") }

        // 2. Vérifie si la partie n'est pas déjà finie
        if (game.status == GameStatus.FINISHED) {
            throw GameAlreadyFinishedException("Game ${game.id} is already finished.") as Throwable
        }

        // 3. Crée et sauvegarde l'erreur
        val newError = GameError(gameEntity = game)
        gameErrorRepository.save(newError)
    }
}