package com.example.mk8dxplaylist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mk8dxplaylist.data.TrackData
import com.example.mk8dxplaylist.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistEditorScreen(
    playlistId: String,
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()
    val playlist = playlists.find { it.id == playlistId } ?: return

    var nameText by remember(playlist.id) { mutableStateOf(playlist.name) }
    val selectedIds = playlist.trackIds.toSet()

    val groupedTracks = remember {
        TrackData.allTracks.groupBy { it.cup }
    }

    // Track filter: "all", "base", "booster"
    var filter by remember { mutableStateOf("all") }

    val visibleGroups = remember(filter) {
        groupedTracks.entries
            .filter { (_, tracks) ->
                when (filter) {
                    "base" -> tracks.first().isBoosterCourse.not()
                    "booster" -> tracks.first().isBoosterCourse
                    else -> true
                }
            }
            .sortedBy { (cup, _) -> TrackData.allCupNames.indexOf(cup) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Playlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val allVisibleIds = visibleGroups.flatMap { it.value }.map { it.id }.toSet()
                    val allSelected = allVisibleIds.isNotEmpty() && allVisibleIds.all { it in selectedIds }
                    IconButton(onClick = {
                        if (allSelected) {
                            // deselect all visible
                            val newIds = selectedIds - allVisibleIds
                            viewModel.savePlaylist(playlist.copy(trackIds = newIds.toList()))
                        } else {
                            // select all visible
                            val newIds = selectedIds + allVisibleIds
                            viewModel.savePlaylist(playlist.copy(trackIds = newIds.toList()))
                        }
                    }) {
                        Icon(Icons.Default.SelectAll, contentDescription = if (allSelected) "Deselect all" else "Select all")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Name field
            item {
                OutlinedTextField(
                    value = nameText,
                    onValueChange = {
                        nameText = it
                        viewModel.renamePlaylist(playlistId, it)
                    },
                    label = { Text("Playlist name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    singleLine = true
                )
            }

            // Track count summary
            item {
                Text(
                    "${selectedIds.size} of ${TrackData.allTracks.size} tracks selected",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Filter chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("all" to "All", "base" to "Base Game", "booster" to "Booster Pass").forEach { (value, label) ->
                        FilterChip(
                            selected = filter == value,
                            onClick = { filter = value },
                            label = { Text(label, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Select All / Deselect All buttons
            item {
                val allVisibleIds = visibleGroups.flatMap { it.value }.map { it.id }.toSet()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val newIds = selectedIds + allVisibleIds
                            viewModel.savePlaylist(playlist.copy(trackIds = newIds.toList()))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Select All", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            val newIds = selectedIds - allVisibleIds
                            viewModel.savePlaylist(playlist.copy(trackIds = newIds.toList()))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Deselect All", fontSize = 12.sp)
                    }
                }
            }

            // Track groups
            visibleGroups.forEach { (cup, tracks) ->
                val isBooster = tracks.first().isBoosterCourse
                val cupSelectedCount = tracks.count { it.id in selectedIds }

                item(key = "header_$cup") {
                    CupHeader(
                        name = cup,
                        isBooster = isBooster,
                        selectedCount = cupSelectedCount,
                        totalCount = tracks.size,
                        onToggleAll = {
                            val allSelected = tracks.all { it.id in selectedIds }
                            val toggled = if (allSelected) selectedIds - tracks.map { it.id }.toSet()
                            else selectedIds + tracks.map { it.id }.toSet()
                            viewModel.savePlaylist(playlist.copy(trackIds = toggled.toList()))
                        }
                    )
                }

                items(tracks, key = { it.id }) { track ->
                    val checked = track.id in selectedIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleTrack(playlistId, track.id) }
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = null,
                            tint = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            track.name,
                            fontSize = 15.sp,
                            color = if (checked) MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CupHeader(
    name: String,
    isBooster: Boolean,
    selectedCount: Int,
    totalCount: Int,
    onToggleAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            .padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isBooster) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
            )
            if (isBooster) {
                Text(
                    "Booster Course Pass",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                )
            }
        }
        Text(
            "$selectedCount/$totalCount",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        TextButton(onClick = onToggleAll) {
            Text(
                if (selectedCount == totalCount) "None" else "All",
                fontSize = 12.sp
            )
        }
    }
}
