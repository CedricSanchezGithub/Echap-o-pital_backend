package org.example.kotlin_back.maboule

import org.example.kotlin_back.game.GameStatus
import org.example.kotlin_back.game.GameNotFoundException
import org.example.kotlin_back.game.GameRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class MabouleService(
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

    fun recordError(gameId: String) {
        val game = gameRepository.findById(gameId)
            .orElseThrow { GameNotFoundException("Game not found with id: $gameId") }

        if (game.status == GameStatus.FINISHED) {
            throw RuntimeException("Game ${game.id} is already finished.")
        }

        val newError = MabouleError(gameEntity = game)
        mabouleRepository.save(newError)

        val totalErrors = mabouleRepository.countByGameEntityId(gameId)
        emitters[gameId]?.send(
            SseEmitter.event()
                .name("error-event")
                .data(MabouleEvent(totalErrors = totalErrors))
        )
    }
}