package com.prajwalch.torrentsearch.models

/** Dark theme options. */
enum class DarkTheme {
    On,
    Off,
    FollowSystem
}

/** Results sort criteria. */
enum class SortCriteria {
    Name,
    Seeders,
    Peers,
    FileSize,
    Date;

    companion object {
        /** The default criteria. */
        val Default = Seeders
    }
}

/** Results sort order. */
enum class SortOrder {
    Ascending,
    Descending;

    companion object {
        /** The default sort order. */
        val Default = Descending
    }
}

/** Defines maximum number of results to be shown. */
data class MaxNumResults(val n: Int) {
    fun isUnlimited() = n == UNLIMITED_N

    companion object {
        private const val UNLIMITED_N = -1

        val Unlimited = MaxNumResults(n = UNLIMITED_N)
    }
}

/**
 * Preferred torrent client configuration.
 * When set to a specific package name, magnet links will be opened directly with that app.
 * When set to None, the system chooser will be used.
 */
sealed class PreferredTorrentClient {
    /** Use system chooser to pick torrent client each time. */
    object SystemChooser : PreferredTorrentClient()
    
    /** Use a specific torrent client identified by its package name. */
    data class Specific(val packageName: String, val displayName: String) : PreferredTorrentClient()
    
    companion object {
        /** Default: Use system chooser. */
        val Default = SystemChooser
        
        /** List of known torrent clients with their package names. */
        val KnownClients = listOf(
            Specific("org.proninyaroslav.libretorrent", "LibreTorrent"),
            Specific("com.gianlu.aria2app", "Aria2App"),
            Specific("com.gopeed", "Gopeed"),
            Specific("com.pikatorrent.PikaTorrent", "PikaTorrent"),
            Specific("ru.yourok.torrserve", "TorrServe"),
            Specific("com.utorrent.client", "µTorrent"),
            Specific("com.bittorrent.client", "BitTorrent"),
            Specific("org.transdroid.lite", "Transdroid"),
            Specific("com.delphicoder.flud", "Flud"),
        )
    }
}