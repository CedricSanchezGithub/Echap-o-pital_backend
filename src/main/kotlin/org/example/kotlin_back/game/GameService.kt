package org.example.kotlin_back.game

import org.example.kotlin_back.entity.GameEntity
import org.example.kotlin_back.entity.GameError
import org.example.kotlin_back.maboule.GameEvent
import org.example.kotlin_back.entity.GameResponse
import org.example.kotlin_back.entity.GameStatus
import org.example.kotlin_back.maboule.MabouleRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val mabouleRepository: MabouleRepository
) {
    private val emitters = ConcurrentHashMap<String, SseEmitter>()

    fun subscribeToGameEvents(gameId: String): SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE)

        emitter.onCompletion { emitters.remove(gameId) }
        emitter.onTimeout { emitters.remove(gameId) }
        emitter.onError { emitters.remove(gameId) }

        emitters[gameId] = emitter

        return emitter
    }

    fun createNewGame(): GameResponse {
        val newGame = GameEntity.Companion.new()
        val savedGame = gameRepository.save(newGame)
        return GameResponse(gameId = savedGame.id)
    }

    fun recordError(gameId: String) {
        val game = gameRepository.findById(gameId)
            .orElseThrow { GameNotFoundException("Game not found with id: $gameId") }

        if (game.status == GameStatus.FINISHED) {
            throw GameAlreadyFinishedException("Game ${game.id} is already finished.")
        }

        val newError = GameError(gameEntity = game)
        mabouleRepository.save(newError)

        val totalErrors = mabouleRepository.countByGameEntityId(gameId)
        emitters[gameId]?.send(
            SseEmitter.event()
                .name("error-event")
                .data(GameEvent(totalErrors = totalErrors))
        )
    }
}