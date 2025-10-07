package org.example.kotlin_back.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "game_errors")
class GameError(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val gameEntity: GameEntity,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)