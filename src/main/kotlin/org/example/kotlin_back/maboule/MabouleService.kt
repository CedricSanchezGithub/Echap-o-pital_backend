package org.example.kotlin_back.maboule

import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class MabouleService {

    private val emitters = ConcurrentHashMap<String, SseEmitter>()

    fun createEmitter(gameId: String): SseEmitter {

        val emitter = SseEmitter(600_000L)

        emitters[gameId] = emitter
        emitter.onCompletion { emitters.remove(gameId) }
        emitter.onTimeout { emitters.remove(gameId) }
        emitter.onError { emitters.remove(gameId) }

        return emitter
    }

    fun sendEvent(gameId: String, data: Any) {
        emitters[gameId]?.send(
            SseEmitter.event()
                .name("game-update")
                .data(data)
        )
    }
}