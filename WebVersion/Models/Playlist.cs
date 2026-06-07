namespace MK8DXPlaylist.Models;

public class Playlist
{
    public string Id { get; set; } = Guid.NewGuid().ToString();
    public string Name { get; set; } = "";
    public List<string> TrackIds { get; set; } = new();
}
