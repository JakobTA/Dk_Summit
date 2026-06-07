namespace MK8DXPlaylist.Data;

using MK8DXPlaylist.Models;

public static class TrackData
{
    private static List<Track> Cup(string cupName, bool isBooster, params string[] names) =>
        names.Select((name, i) => new Track(
            Id: $"{cupName.ToLower().Replace(" ", "_")}_{i}",
            Name: name,
            Cup: cupName,
            IsBoosterCourse: isBooster
        )).ToList();

    // ── Base Game: Nitro Cups ────────────────────────────────────────────────

    private static readonly List<Track> MushroomCup = Cup("Mushroom Cup", false,
        "Mario Kart Stadium", "Water Park", "Sweet Sweet Canyon", "Thwomp Ruins");

    private static readonly List<Track> FlowerCup = Cup("Flower Cup", false,
        "Mario Circuit", "Toad Harbor", "Twisted Mansion", "Shy Guy Falls");

    private static readonly List<Track> StarCup = Cup("Star Cup", false,
        "Sunshine Airport", "Dolphin Shoals", "Electrodrome", "Mount Wario");

    private static readonly List<Track> SpecialCup = Cup("Special Cup", false,
        "Cloudtop Cruise", "Bone-Dry Dunes", "Bowser's Castle", "Rainbow Road");

    // ── Base Game: Retro Cups ────────────────────────────────────────────────

    private static readonly List<Track> ShellCup = Cup("Shell Cup", false,
        "Moo Moo Meadows (Wii)", "Mario Circuit (GBA)", "Cheep Cheep Beach (DS)", "Toad's Turnpike (N64)");

    private static readonly List<Track> BananaCup = Cup("Banana Cup", false,
        "Dry Dry Desert (GCN)", "Donut Plains 3 (SNES)", "Royal Raceway (N64)", "DK Jungle (3DS)");

    private static readonly List<Track> LeafCup = Cup("Leaf Cup", false,
        "Wario Stadium (DS)", "Sherbet Land (GCN)", "Music Park (3DS)", "Yoshi Valley (N64)");

    private static readonly List<Track> LightningCup = Cup("Lightning Cup", false,
        "Koopa Troopa Beach (N64)", "Mario Circuit (GCN)", "Maple Treeway (Wii)", "Grumble Volcano (Wii)");

    // ── DLC Cups (bundled with MK8DX) ───────────────────────────────────────

    private static readonly List<Track> EggCup = Cup("Egg Cup", false,
        "Yoshi Circuit (GCN)", "Excitebike Arena", "Dragon Driftway", "Mute City");

    private static readonly List<Track> TriforceCup = Cup("Triforce Cup", false,
        "Wario's Gold Mine (Wii)", "Rainbow Road (SNES)", "Ice Ice Outpost", "Hyrule Circuit");

    private static readonly List<Track> CrossingCup = Cup("Crossing Cup", false,
        "Baby Park (GCN)", "Cheese Land (GBA)", "Wild Woods", "Animal Crossing");

    private static readonly List<Track> BellCup = Cup("Bell Cup", false,
        "Neo Bowser City (3DS)", "Ribbon Road (GBA)", "Super Bell Subway", "Big Blue");

    // ── Booster Course Pass: Wave 1 ──────────────────────────────────────────

    private static readonly List<Track> GoldenDashCup = Cup("Golden Dash Cup", true,
        "Paris Promenade (Tour)", "Toad Circuit (3DS)", "Choco Mountain (N64)", "Coconut Mall (Wii)");

    private static readonly List<Track> LuckyCatCup = Cup("Lucky Cat Cup", true,
        "Tokyo Blur (Tour)", "Shroom Ridge (DS)", "Sky Garden (GBA)", "Ninja Hideaway (Tour)");

    // ── Booster Course Pass: Wave 2 ──────────────────────────────────────────

    private static readonly List<Track> TurnipCup = Cup("Turnip Cup", true,
        "New York Minute (Tour)", "Mario Circuit 3 (SNES)", "Kalimari Desert (N64)", "Waluigi Pinball (DS)");

    private static readonly List<Track> PropellerCup = Cup("Propeller Cup", true,
        "Sydney Sprint (Tour)", "Snow Land (GBA)", "Mushroom Gorge (Wii)", "Sky-High Sundae (Tour)");

    // ── Booster Course Pass: Wave 3 ──────────────────────────────────────────

    private static readonly List<Track> RockCup = Cup("Rock Cup", true,
        "London Loop (Tour)", "Boo Lake (GBA)", "Rock Rock Mountain (3DS)", "Daisy Circuit (Wii)");

    private static readonly List<Track> MoonCup = Cup("Moon Cup", true,
        "Berlin Byways (Tour)", "Peach Gardens (DS)", "Merry Mountain (Tour)", "Rainbow Road (3DS)");

    // ── Booster Course Pass: Wave 4 ──────────────────────────────────────────

    private static readonly List<Track> FruitCup = Cup("Fruit Cup", true,
        "Amsterdam Drift (Tour)", "Riverside Park (GBA)", "DK Summit (Wii)", "Yoshi's Island (Tour)");

    private static readonly List<Track> BoomerangCup = Cup("Boomerang Cup", true,
        "Bangkok Rush (Tour)", "Mario Circuit (DS)", "Waluigi Stadium (GCN)", "Singapore Speedway (Tour)");

    // ── Booster Course Pass: Wave 5 ──────────────────────────────────────────

    private static readonly List<Track> FeatherCup = Cup("Feather Cup", true,
        "Athens Dash (Tour)", "Daisy Cruiser (GCN)", "Moonview Highway (Wii)", "Squeaky Clean Sprint (Tour)");

    private static readonly List<Track> CherryCup = Cup("Cherry Cup", true,
        "Los Angeles Laps (Tour)", "Sunset Wilds (GBA)", "Koopa Cape (Wii)", "Vancouver Velocity (Tour)");

    // ── Booster Course Pass: Wave 6 ──────────────────────────────────────────

    private static readonly List<Track> AcornCup = Cup("Acorn Cup", true,
        "Rome Avanti (Tour)", "DK Mountain (GCN)", "Maze Castle (Tour)", "Piranha Plant Cove (Tour)");

    private static readonly List<Track> SpinyCup = Cup("Spiny Cup", true,
        "Rosalina's Ice World (3DS)", "Bowser Castle 3 (SNES)", "Rainbow Road (Wii)", "Yoshi's Island (GBA)");

    // ── All tracks ───────────────────────────────────────────────────────────

    public static readonly IReadOnlyList<Track> AllTracks = new[]
    {
        MushroomCup, FlowerCup, StarCup, SpecialCup,
        ShellCup, BananaCup, LeafCup, LightningCup,
        EggCup, TriforceCup, CrossingCup, BellCup,
        GoldenDashCup, LuckyCatCup,
        TurnipCup, PropellerCup,
        RockCup, MoonCup,
        FruitCup, BoomerangCup,
        FeatherCup, CherryCup,
        AcornCup, SpinyCup
    }.SelectMany(c => c).ToList().AsReadOnly();

    public static readonly IReadOnlyList<string> AllCupNames =
        AllTracks.Select(t => t.Cup).Distinct().ToList().AsReadOnly();

    public static Track? TrackById(string id) =>
        AllTracks.FirstOrDefault(t => t.Id == id);

    public static List<Track> TracksForPlaylist(IEnumerable<string> trackIds) =>
        trackIds.Select(TrackById).OfType<Track>().ToList();
}
