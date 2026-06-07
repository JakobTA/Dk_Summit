package com.example.mk8dxplaylist.data

import java.util.UUID

data class Playlist(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val trackIds: List<String> = emptyList()
)
