package org.example.kotlin_back.maboule

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/maboule")
class MabouleController {

    companion object {
        private val log = LoggerFactory.getLogger(MabouleController::class.java)
    }

    private var sseEmitter: SseEmitter? = null
    private val errorCount = AtomicLong(0)

    @GetMapping("/events")
    fun subscribe(): SseEmitter {
        log.info("New frontend client connected via SSE.")
        val emitter = SseEmitter(Long.MAX_VALUE)
        emitter.onCompletion {
            log.info("SSE client disconnected.")
            sseEmitter = null
        }
        emitter.onTimeout { sseEmitter = null }
        emitter.onError { sseEmitter = null }

        sseEmitter = emitter

        sseEmitter?.send(
            SseEmitter.event()
                .name("state-update")
                .data(mapOf("totalErrors" to errorCount.get()))
        )

        return emitter
    }

    @PostMapping("/error")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun triggerError() {
        log.info("-> Received error trigger from Raspberry Pi.")
        val newCount = errorCount.incrementAndGet()

        log.info("<- Sending error-event to frontend with totalErrors: {}", newCount)
        sseEmitter?.send(
            SseEmitter.event()
                .name("error-event")
                .data(mapOf("totalErrors" to newCount))
        )
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reset() {
        log.info("!!! Received request to RESET game state.")
        errorCount.set(0)

        log.info("<- Sending state-update to frontend with totalErrors: 0")
        sseEmitter?.send(
            SseEmitter.event()
                .name("state-update")
                .data(mapOf("totalErrors" to 0))
        )
    }
}