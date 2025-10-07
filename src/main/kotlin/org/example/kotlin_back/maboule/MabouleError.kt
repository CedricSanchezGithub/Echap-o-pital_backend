package org.example.kotlin_back.maboule

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.kotlin_back.game.GameEntity
import java.time.LocalDateTime

@Entity
@Table(name = "game_errors")
class MabouleError(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val gameEntity: GameEntity,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)