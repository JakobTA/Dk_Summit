namespace MK8DXPlaylist.Models;

public class RandomizerState
{
    public string PlaylistId { get; set; } = "";
    public string PlaylistName { get; set; } = "";
    public Track? CurrentTrack { get; set; }
    public List<Track> Queue { get; set; } = new();
    public int TotalTracks { get; set; }
    public int TrackCount { get; set; }

    public int TracksRemaining => Queue.Count;
    public bool IsDone => CurrentTrack is null && Queue.Count == 0;
    public int TrackNumber => TotalTracks - TracksRemaining;
}
