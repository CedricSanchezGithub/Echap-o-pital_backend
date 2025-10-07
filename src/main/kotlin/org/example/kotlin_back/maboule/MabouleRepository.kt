package org.example.kotlin_back.maboule

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MabouleRepository : JpaRepository<MabouleError, Long> {
    fun countByGameEntityId(gameId: String): Long
}