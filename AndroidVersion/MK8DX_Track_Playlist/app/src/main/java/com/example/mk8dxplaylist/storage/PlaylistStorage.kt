package com.example.mk8dxplaylist.storage

import android.content.Context
import com.example.mk8dxplaylist.data.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistStorage(context: Context) {
    private val prefs = context.getSharedPreferences("playlists", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "playlist_list"

    fun load(): List<Playlist> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<Playlist>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun save(playlists: List<Playlist>) {
        prefs.edit().putString(key, gson.toJson(playlists)).apply()
    }
}
