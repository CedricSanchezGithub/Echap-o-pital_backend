package org.example.kotlin_back.maboule

import org.example.kotlin_back.entity.GameError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MabouleRepository : JpaRepository<GameError, Long> {
    fun countByGameEntityId(gameId: String): Long
}