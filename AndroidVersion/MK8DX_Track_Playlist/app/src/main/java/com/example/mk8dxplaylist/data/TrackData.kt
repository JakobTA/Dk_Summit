package com.example.mk8dxplaylist.data

// Full MK8DX track list including the Booster Course Pass (96 tracks total).
// Cup assignments for BCP waves should be verified against the official Nintendo track list.
object TrackData {

    private fun cup(cupName: String, isBooster: Boolean, vararg names: String): List<Track> =
        names.mapIndexed { i, name ->
            Track(
                id = "${cupName.lowercase().replace(" ", "_")}_$i",
                name = name,
                cup = cupName,
                isBoosterCourse = isBooster
            )
        }

    // ── Base Game: Nitro Cups ────────────────────────────────────────────────

    private val mushroomCup = cup(
        "Mushroom Cup", false,
        "Mario Kart Stadium",
        "Water Park",
        "Sweet Sweet Canyon",
        "Thwomp Ruins"
    )

    private val flowerCup = cup(
        "Flower Cup", false,
        "Mario Circuit",
        "Toad Harbor",
        "Twisted Mansion",
        "Shy Guy Falls"
    )

    private val starCup = cup(
        "Star Cup", false,
        "Sunshine Airport",
        "Dolphin Shoals",
        "Electrodrome",
        "Mount Wario"
    )

    private val specialCup = cup(
        "Special Cup", false,
        "Cloudtop Cruise",
        "Bone-Dry Dunes",
        "Bowser's Castle",
        "Rainbow Road"
    )

    // ── Base Game: Retro Cups ────────────────────────────────────────────────

    private val shellCup = cup(
        "Shell Cup", false,
        "Moo Moo Meadows (Wii)",
        "Mario Circuit (GBA)",
        "Cheep Cheep Beach (DS)",
        "Toad's Turnpike (N64)"
    )

    private val bananaCup = cup(
        "Banana Cup", false,
        "Dry Dry Desert (GCN)",
        "Donut Plains 3 (SNES)",
        "Royal Raceway (N64)",
        "DK Jungle (3DS)"
    )

    private val leafCup = cup(
        "Leaf Cup", false,
        "Wario Stadium (DS)",
        "Sherbet Land (GCN)",
        "Music Park (3DS)",
        "Yoshi Valley (N64)"
    )

    private val lightningCup = cup(
        "Lightning Cup", false,
        "Koopa Troopa Beach (N64)",
        "Mario Circuit (GCN)",
        "Maple Treeway (Wii)",
        "Grumble Volcano (Wii)"
    )

    // ── DLC Cups (bundled with MK8DX) ───────────────────────────────────────

    private val eggCup = cup(
        "Egg Cup", false,
        "Yoshi Circuit (GCN)",
        "Excitebike Arena",
        "Dragon Driftway",
        "Mute City"
    )

    private val triforceCup = cup(
        "Triforce Cup", false,
        "Wario's Gold Mine (Wii)",
        "Rainbow Road (SNES)",
        "Ice Ice Outpost",
        "Hyrule Circuit"
    )

    private val crossingCup = cup(
        "Crossing Cup", false,
        "Baby Park (GCN)",
        "Cheese Land (GBA)",
        "Wild Woods",
        "Animal Crossing"
    )

    private val bellCup = cup(
        "Bell Cup", false,
        "Neo Bowser City (3DS)",
        "Ribbon Road (GBA)",
        "Super Bell Subway",
        "Big Blue"
    )

    // ── Booster Course Pass: Wave 1 ──────────────────────────────────────────

    private val goldenDashCup = cup(
        "Golden Dash Cup", true,
        "Paris Promenade (Tour)",
        "Toad Circuit (3DS)",
        "Choco Mountain (N64)",
        "Coconut Mall (Wii)"
    )

    private val luckyCatCup = cup(
        "Lucky Cat Cup", true,
        "Tokyo Blur (Tour)",
        "Shroom Ridge (DS)",
        "Sky Garden (GBA)",
        "Ninja Hideaway (Tour)"
    )

    // ── Booster Course Pass: Wave 2 ──────────────────────────────────────────

    private val turnipCup = cup(
        "Turnip Cup", true,
        "New York Minute (Tour)",
        "Mario Circuit 3 (SNES)",
        "Kalimari Desert (N64)",
        "Waluigi Pinball (DS)"
    )

    private val propellerCup = cup(
        "Propeller Cup", true,
        "Sydney Sprint (Tour)",
        "Snow Land (GBA)",
        "Mushroom Gorge (Wii)",
        "Sky-High Sundae (Tour)"
    )

    // ── Booster Course Pass: Wave 3 ──────────────────────────────────────────

    private val rockCup = cup(
        "Rock Cup", true,
        "London Loop (Tour)",
        "Boo Lake (GBA)",
        "Rock Rock Mountain (3DS)",
        "Daisy Circuit (Wii)"
    )

    private val moonCup = cup(
        "Moon Cup", true,
        "Berlin Byways (Tour)",
        "Peach Gardens (DS)",
        "Merry Mountain (Tour)",
        "Rainbow Road (3DS)"
    )

    // ── Booster Course Pass: Wave 4 ──────────────────────────────────────────

    private val fruitCup = cup(
        "Fruit Cup", true,
        "Amsterdam Drift (Tour)",
        "Riverside Park (GBA)",
        "DK Summit (Wii)",
        "Yoshi's Island (Tour)"
    )

    private val boomerangCup = cup(
        "Boomerang Cup", true,
        "Bangkok Rush (Tour)",
        "Mario Circuit (DS)",
        "Waluigi Stadium (GCN)",
        "Singapore Speedway (Tour)"
    )

    // ── Booster Course Pass: Wave 5 ──────────────────────────────────────────

    private val featherCup = cup(
        "Feather Cup", true,
        "Athens Dash (Tour)",
        "Daisy Cruiser (GCN)",
        "Moonview Highway (Wii)",
        "Squeaky Clean Sprint (Tour)"
    )

    private val cherryCup = cup(
        "Cherry Cup", true,
        "Los Angeles Laps (Tour)",
        "Sunset Wilds (GBA)",
        "Koopa Cape (Wii)",
        "Vancouver Velocity (Tour)"
    )

    // ── Booster Course Pass: Wave 6 ──────────────────────────────────────────

    private val acornCup = cup(
        "Acorn Cup", true,
        "Rome Avanti (Tour)",
        "DK Mountain (GCN)",
        "Maze Castle (Tour)",
        "Piranha Plant Cove (Tour)"
    )

    private val spinyCup = cup(
        "Spiny Cup", true,
        "Rosalina's Ice World (3DS)",
        "Bowser Castle 3 (SNES)",
        "Rainbow Road (Wii)",
        "Yoshi's Island (GBA)"
    )

    // ── All tracks ───────────────────────────────────────────────────────────

    val allTracks: List<Track> = listOf(
        mushroomCup, flowerCup, starCup, specialCup,
        shellCup, bananaCup, leafCup, lightningCup,
        eggCup, triforceCup, crossingCup, bellCup,
        goldenDashCup, luckyCatCup,
        turnipCup, propellerCup,
        rockCup, moonCup,
        fruitCup, boomerangCup,
        featherCup, cherryCup,
        acornCup, spinyCup
    ).flatten()

    val allCupNames: List<String> = allTracks.map { it.cup }.distinct()

    fun trackById(id: String): Track? = allTracks.find { it.id == id }

    fun tracksForPlaylist(trackIds: List<String>): List<Track> =
        trackIds.mapNotNull { id -> trackById(id) }
}
