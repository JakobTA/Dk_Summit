package com.example.mk8dxplaylist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mk8dxplaylist.data.Playlist
import com.example.mk8dxplaylist.data.Track
import com.example.mk8dxplaylist.data.TrackData
import com.example.mk8dxplaylist.storage.PlaylistStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class RandomizerState(
    val playlistId: String,
    val playlistName: String,
    val currentTrack: Track?,
    val queue: List<Track>,
    val totalTracks: Int,
    val trackCount: Int
) {
    val tracksRemaining: Int get() = queue.size
    val isDone: Boolean get() = currentTrack == null && queue.isEmpty()
    val isStarted: Boolean get() = currentTrack != null || queue.size < totalTracks
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = PlaylistStorage(application)

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _randomizerState = MutableStateFlow<RandomizerState?>(null)
    val randomizerState: StateFlow<RandomizerState?> = _randomizerState.asStateFlow()

    init {
        _playlists.value = storage.load()
    }

    // ── Playlist management ──────────────────────────────────────────────────

    fun createPlaylist(name: String): String {
        val playlist = Playlist(id = UUID.randomUUID().toString(), name = name)
        _playlists.update { it + playlist }
        persist()
        return playlist.id
    }

    fun savePlaylist(playlist: Playlist) {
        _playlists.update { current ->
            val idx = current.indexOfFirst { it.id == playlist.id }
            if (idx >= 0) current.toMutableList().also { it[idx] = playlist }
            else current + playlist
        }
        persist()
    }

    fun deletePlaylist(id: String) {
        _playlists.update { it.filter { p -> p.id != id } }
        persist()
    }

    fun getPlaylist(id: String): Playlist? = _playlists.value.find { it.id == id }

    fun toggleTrack(playlistId: String, trackId: String) {
        _playlists.update { current ->
            current.map { playlist ->
                if (playlist.id != playlistId) playlist
                else {
                    val ids = if (trackId in playlist.trackIds)
                        playlist.trackIds - trackId
                    else
                        playlist.trackIds + trackId
                    playlist.copy(trackIds = ids)
                }
            }
        }
        persist()
    }

    fun renamePlaylist(playlistId: String, newName: String) {
        _playlists.update { current ->
            current.map { if (it.id == playlistId) it.copy(name = newName) else it }
        }
        persist()
    }

    private fun persist() {
        storage.save(_playlists.value)
    }

    // ── Randomizer ───────────────────────────────────────────────────────────

    fun startRandomizer(playlistId: String, trackCount: Int) {
        val playlist = getPlaylist(playlistId) ?: return
        val tracks = TrackData.tracksForPlaylist(playlist.trackIds).shuffled().take(trackCount)
        if (tracks.isEmpty()) return

        _randomizerState.value = RandomizerState(
            playlistId = playlistId,
            playlistName = playlist.name,
            currentTrack = tracks.first(),
            queue = tracks.drop(1),
            totalTracks = tracks.size,
            trackCount = trackCount
        )
    }

    fun nextTrack() {
        val state = _randomizerState.value ?: return
        if (state.queue.isEmpty()) {
            _randomizerState.value = state.copy(currentTrack = null)
            return
        }
        _randomizerState.value = state.copy(
            currentTrack = state.queue.first(),
            queue = state.queue.drop(1)
        )
    }

    fun resetRandomizer() {
        val state = _randomizerState.value ?: return
        startRandomizer(state.playlistId, state.trackCount)
    }

    fun clearRandomizer() {
        _randomizerState.value = null
    }
}
