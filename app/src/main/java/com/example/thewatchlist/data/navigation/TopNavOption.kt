package com.example.thewatchlist.data.navigation

enum class TopNavOption {
    ToWatch,
    Watching,
    History;

    override fun toString(): String {
        return when (this) {
            ToWatch -> "To Watch"
            Watching -> "Watching"
            History -> "History"
        }
    }
}
