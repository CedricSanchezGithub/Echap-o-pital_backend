package org.example.kotlin_back.game

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "games")
class GameEntity(
    @Id
    val id: String,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time")
    var endTime: LocalDateTime?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: GameStatus
) {
    companion object {
        fun new(): GameEntity {
            return GameEntity(
                id = UUID.randomUUID().toString(),
                startTime = LocalDateTime.now(),
                status = GameStatus.IN_PROGRESS,
                endTime = null
            )
        }
    }
}

enum class GameStatus {
    IN_PROGRESS,
    FINISHED
}