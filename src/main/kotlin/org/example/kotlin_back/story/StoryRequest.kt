package org.example.kotlin_back.story

data class StoryRequest(
    val symptome: String,
    val salle: String,
    val etat: String
)