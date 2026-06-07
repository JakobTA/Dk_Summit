package com.example.mk8dxplaylist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mk8dxplaylist.data.Playlist
import com.example.mk8dxplaylist.viewmodel.AppViewModel

private val STANDARD_RACE_COUNTS = listOf(4, 6, 8, 12, 16, 24, 32, 48)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onEditPlaylist: (String) -> Unit,
    onNewPlaylist: () -> Unit,
    onStartRandomizer: (String) -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()
    var deleteTarget by remember { mutableStateOf<Playlist?>(null) }
    var playTarget by remember { mutableStateOf<Playlist?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("MK8DX", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Track Playlists", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewPlaylist,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Playlist")
            }
        }
    ) { padding ->
        if (playlists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No playlists yet", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    Spacer(Modifier.height(8.dp))
                    Text("Tap + to create one", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistCard(
                        playlist = playlist,
                        onEdit = { onEditPlaylist(playlist.id) },
                        onDelete = { deleteTarget = playlist },
                        onPlay = { playTarget = playlist }
                    )
                }
            }
        }
    }

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Delete playlist?") },
            text = { Text("\"${target.name}\" will be permanently deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePlaylist(target.id)
                    deleteTarget = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("Cancel") }
            }
        )
    }

    playTarget?.let { target ->
        TrackCountDialog(
            playlist = target,
            onDismiss = { playTarget = null },
            onConfirm = { count ->
                playTarget = null
                viewModel.startRandomizer(target.id, count)
                onStartRandomizer(target.id)
            }
        )
    }
}

@Composable
private fun TrackCountDialog(
    playlist: Playlist,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val availableSize = playlist.trackIds.size
    // Standard options below the full size, plus the full size itself (deduplicated)
    val options = (STANDARD_RACE_COUNTS.filter { it < availableSize } + availableSize).distinct()

    var selected by remember { mutableIntStateOf(options.last()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("How many tracks?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "\"${playlist.name}\"",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                // Chips in rows of 4
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.chunked(4).forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { count ->
                                val isAll = count == availableSize
                                val label = if (isAll && count !in STANDARD_RACE_COUNTS) "All\n($count)" else "$count"
                                FilterChip(
                                    selected = selected == count,
                                    onClick = { selected = count },
                                    label = {
                                        Text(
                                            label,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Pad incomplete rows so chips stay the same width
                            repeat(4 - row.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selected) }) {
                Icon(Icons.Default.Shuffle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Start")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun PlaylistCard(
    playlist: Playlist,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    playlist.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${playlist.trackIds.size} track${if (playlist.trackIds.size == 1) "" else "s"}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
            }
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = onPlay,
                enabled = playlist.trackIds.isNotEmpty(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Shuffle, contentDescription = "Start randomizer")
            }
        }
    }
}
