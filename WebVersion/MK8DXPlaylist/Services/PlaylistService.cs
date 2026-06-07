namespace MK8DXPlaylist.Services;

using MK8DXPlaylist.Data;
using MK8DXPlaylist.Models;

public class PlaylistService(LocalStorageService storage)
{
    private List<Playlist> _playlists = new();
    private bool _initialized = false;

    public event Action? OnChange;

    public IReadOnlyList<Playlist> Playlists => _playlists.AsReadOnly();
    public RandomizerState? RandomizerState { get; private set; }

    public async Task InitializeAsync()
    {
        if (_initialized) return;
        _playlists = await storage.GetItemAsync<List<Playlist>>("playlists") ?? new();
        _initialized = true;
        NotifyChanged();
    }

    // ── Playlist management ──────────────────────────────────────────────────

    public async Task<string> CreatePlaylistAsync(string name)
    {
        var playlist = new Playlist { Name = name };
        _playlists.Add(playlist);
        await PersistAsync();
        NotifyChanged();
        return playlist.Id;
    }

    public async Task DeletePlaylistAsync(string id)
    {
        _playlists.RemoveAll(p => p.Id == id);
        await PersistAsync();
        NotifyChanged();
    }

    public Playlist? GetPlaylist(string id) =>
        _playlists.FirstOrDefault(p => p.Id == id);

    public async Task ToggleTrackAsync(string playlistId, string trackId)
    {
        var playlist = GetPlaylist(playlistId);
        if (playlist is null) return;
        if (playlist.TrackIds.Contains(trackId))
            playlist.TrackIds.Remove(trackId);
        else
            playlist.TrackIds.Add(trackId);
        await PersistAsync();
        NotifyChanged();
    }

    public async Task SetAllTracksInCupAsync(string playlistId, string cupName, bool selected)
    {
        var playlist = GetPlaylist(playlistId);
        if (playlist is null) return;
        var cupTrackIds = TrackData.AllTracks
            .Where(t => t.Cup == cupName)
            .Select(t => t.Id)
            .ToList();
        if (selected)
        {
            foreach (var id in cupTrackIds)
                if (!playlist.TrackIds.Contains(id))
                    playlist.TrackIds.Add(id);
        }
        else
        {
            playlist.TrackIds.RemoveAll(id => cupTrackIds.Contains(id));
        }
        await PersistAsync();
        NotifyChanged();
    }

    public async Task RenamePlaylistAsync(string playlistId, string newName)
    {
        var playlist = GetPlaylist(playlistId);
        if (playlist is null) return;
        playlist.Name = newName;
        await PersistAsync();
        NotifyChanged();
    }

    // ── Randomizer ───────────────────────────────────────────────────────────

    public void StartRandomizer(string playlistId, int trackCount)
    {
        var playlist = GetPlaylist(playlistId);
        if (playlist is null) return;
        var tracks = TrackData.TracksForPlaylist(playlist.TrackIds)
            .OrderBy(_ => Random.Shared.Next())
            .Take(trackCount)
            .ToList();
        if (tracks.Count == 0) return;

        RandomizerState = new RandomizerState
        {
            PlaylistId = playlistId,
            PlaylistName = playlist.Name,
            CurrentTrack = tracks[0],
            Queue = tracks.Skip(1).ToList(),
            TotalTracks = tracks.Count,
            TrackCount = trackCount
        };
        NotifyChanged();
    }

    public void NextTrack()
    {
        if (RandomizerState is null) return;
        if (RandomizerState.Queue.Count == 0)
        {
            RandomizerState.CurrentTrack = null;
        }
        else
        {
            RandomizerState.CurrentTrack = RandomizerState.Queue[0];
            RandomizerState.Queue.RemoveAt(0);
        }
        NotifyChanged();
    }

    public void ResetRandomizer()
    {
        if (RandomizerState is null) return;
        StartRandomizer(RandomizerState.PlaylistId, RandomizerState.TrackCount);
    }

    public void ClearRandomizer()
    {
        RandomizerState = null;
        NotifyChanged();
    }

    private void NotifyChanged() => OnChange?.Invoke();

    private async Task PersistAsync() =>
        await storage.SetItemAsync("playlists", _playlists);
}
