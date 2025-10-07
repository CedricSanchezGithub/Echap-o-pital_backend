package org.example.kotlin_back.repository

import org.example.kotlin_back.entity.GameError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameErrorRepository : JpaRepository<GameError, Long>