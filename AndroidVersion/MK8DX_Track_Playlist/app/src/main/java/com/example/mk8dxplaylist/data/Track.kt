package com.example.mk8dxplaylist.data

data class Track(
    val id: String,
    val name: String,
    val cup: String,
    val isBoosterCourse: Boolean = false
)
