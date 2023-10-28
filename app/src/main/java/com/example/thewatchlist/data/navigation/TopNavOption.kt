package com.example.thewatchlist.data.navigation

/**
 * Enum class representing the top navigation options for different media status.
 */
enum class TopNavOption {
    ToWatch,
    Watching,
    History;

    /**
     * Overrides the default toString method to provide more user-friendly labels for each option.
     *
     * @return A user-friendly label for the top navigation option.
     */
    override fun toString(): String {
        return when (this) {
            ToWatch -> "To Watch"
            Watching -> "Watching"
            History -> "History"
        }
    }
}
