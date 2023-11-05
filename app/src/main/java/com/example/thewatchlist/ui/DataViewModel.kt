package com.example.thewatchlist.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.thewatchlist.WatchlistApplication
import com.example.thewatchlist.data.Episode
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.MediaRepository
import com.example.thewatchlist.data.SearchStatus
import com.example.thewatchlist.data.Season
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.data.persistence.StorageRepository
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * ViewModel class responsible for managing data and interactions with the media repository.
 *
 * @param mediaRepository The repository used for retrieving and updating media data.
 */
class DataViewModel(
    private val mediaRepository: MediaRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {
    // Property for tracking the search status
    var searchStatus: SearchStatus by mutableStateOf(SearchStatus.NoAction)
        private set

    // Property for holding the details of a selected media item
    var detailsMediaItem: Media? by mutableStateOf(null)
        private set

    // List of media items in the watchlist
    var mediaList: SnapshotStateList<Media> = mutableStateListOf()
        private set

    // List for storing search results
    var searchResults: SnapshotStateList<Media>? = mutableStateListOf()

    init {
        loadMediaList()
    }

    // add one or more media entries to the database
    private fun saveToDb(vararg media: Media) {
        viewModelScope.launch {
            Log.d("storage", "Adding ${media.count()} media entries to db.")
            try {
                storageRepository.insert(*media)
            }
            catch (e: Exception) {
                Log.e("storage", "Failed to add entries: ${e.message}")
            }
        }
    }

    // update one or more media entries in the database
    private fun updateDbEntry(vararg media: Media) {
        viewModelScope.launch {
            Log.d("storage", "Updating ${media.count()} media entries in db.")
            try {
                storageRepository.update(*media)
            }
            catch (e: Exception) {
                Log.e("storage", "Failed to update entries: ${e.message}")
            }
        }
    }

    // remove one or more media entries from the database
    private fun deleteDbEntry(vararg media: Media) {
        viewModelScope.launch {
            Log.d("storage", "Removing ${media.count()} media entries from db.")
            try {
                storageRepository.delete(*media)
            }
            catch (e: Exception) {
                Log.e("storage", "Failed to delete entries: ${e.message}")
            }
        }
    }

    // load all items from the database
    private fun loadMediaList() {
        viewModelScope.launch {
            mediaList = SnapshotStateList()
            mediaList.addAll(storageRepository.getAllMovies())
            mediaList.addAll(storageRepository.getAllSeries())
            Log.d("storage", "Loaded ${mediaList.count()} media entries from database.")
        }
    }

    /**
     * Function to search for media on TMDB based on a given title.
     *
     * @param title The title to search for.
     */
    suspend fun searchTmdb(title: String) {
        searchStatus = SearchStatus.Loading
        viewModelScope.launch {
            searchStatus = try {
                searchResults = mediaRepository.getMultiMedia(title)?.toMutableStateList()
                when {
                    searchResults.isNullOrEmpty() -> {
                        if (searchResults == null) {
                            SearchStatus.Error("Could not reach the remote server. Please verify your network connection")
                        } else {
                            SearchStatus.Error("Could not find the movie or show you are looking for. Please check for spelling errors in search terms")
                        }
                    }
                    else -> SearchStatus.Success
                }
            } catch (e: IOException) {
                SearchStatus.Error("Unexpected response from remote server. Please try again later")
            } catch (e: NullPointerException) {
                SearchStatus.Error("Unexpected error")
            }
        }
    }

    /**
     * Function to set the checkmark for a season in a TV series.
     *
     * @param checked Whether the season is marked as seen or not.
     * @param tv The TV series to update.
     * @param season The season to update.
     */
    fun setSeasonCheckmark(checked: Boolean, tv: TV, season: Season) {
        // Update the episodes in the season
        val updatedEpisodes = season.episodes.map {
            it.copy(seen = checked)
        }
        // Update the TV series with the modified season
        val updatedTv = tv.copy(seasons = tv.seasons.map {
            if (it == season) season.copy(episodes = updatedEpisodes) else it
        })
        updateMediaEntry(updatedTv)
    }

    /**
     * Function to set the checkmark for an episode in a TV series.
     *
     * @param checked Whether the episode is marked as seen or not.
     * @param tv The TV series to update.
     * @param episode The episode to update.
     */
    fun setEpisodeCheckmark(checked: Boolean, tv: TV, episode: Episode) {
        // Update the TV series with the modified episode
        val updatedTv = tv.copy(seasons = tv.seasons.map { season ->
            season.copy(episodes = season.episodes.map {
                if (it == episode) episode.copy(seen = checked) else it
            })
        })
        updateMediaEntry(updatedTv)
    }

    /**
     * Function to update a media entry in the data model.
     *
     * @param media The media item to update.
     */
    private fun updateMediaEntry(media: Media) {
        // Update the media item in the watchlist
        val index = mediaList.indexOfFirst { media.id == it.id }
        if (index >= 0) {
            mediaList[index] = media
        }
        // Update the media item in the detail screen
        if (detailsMediaItem?.id == media.id) {
            detailsMediaItem = media
        }
        // Update the media item in search results
        searchResults?.indexOfFirst { media.id == it.id }?.let {
            if (it != -1) {
                searchResults!![it] = media
            }
        }

        // update media entry in database
        updateDbEntry(media)
    }

    /**
     *  Function to get the next unwatched episode
     */
    fun getNextUnwatchedEpisode(tv: TV): Episode? {
        return tv.seasons.flatMap { it.episodes }
            .firstOrNull { it.seasonNumber > 0 && !it.seen }
    }

    /**
     * Function to set the currently active details media item.
     *
     * @param media The media item to set as the active details item.
     */
    fun setActiveDetailsMediaItem(media: Media) {
        detailsMediaItem = media
    }

    /**
     * Function to check if a media item is in the watchlist.
     *
     * @param media The media item to check.
     * @return true if the media item is in the watchlist, false otherwise.
     */
    fun isInWatchlist(media: Media): Boolean {
        return mediaList.find { media.id == it.id && it.status != TopNavOption.History } != null
    }

    /**
     * Function to move a media item to a specific tab in the watchlist.
     *
     * @param media The media item to move.
     * @param tab The target tab to move the media item to.
     */
    fun moveMediaTo(media: Media, tab: TopNavOption?) {
        mediaList.remove(media)
        if (tab != null) {
            media.status = tab
            mediaList.add(media)
        }

        // update local database
        when (tab) {
            TopNavOption.ToWatch -> saveToDb(media)
            null -> deleteDbEntry(media)
            else -> updateDbEntry(media)
        }
    }

    /**
     * Function to update season information for a TV series.
     *
     * @param tv The TV series to update.
     */
    suspend fun updateSeasonInfo(tv: TV) {
        try {
            val downloadedShow = mediaRepository.getSeries(tv.id)
            downloadedShow?.seasons?.forEach { season ->
                if (tv.seasons.find { it.id == season.id } == null) {
                    tv.seasons += season
                }
            }
            updateMediaEntry(tv)
        } catch (e: Exception) {
            Log.d("app", "failed to update season information")
        }
    }

    /**
     * Function to update episodes for a TV series.
     *
     * @param tv The TV series to update.
     */
    suspend fun updateEpisodes(tv: TV) {
        try {
            val newTv = tv.copy(seasons = tv.seasons.map { season ->
                mediaRepository.getEpisodes(tv.id, season.seasonNumber)!!.let { episodes ->
                    season.copy(episodes = episodes.map { episode ->
                        season.episodes.find { episode.episodeNumber == it.episodeNumber } ?: episode
                    }.sortedBy { it.episodeNumber })
                }
            })
            updateMediaEntry(newTv)
        } catch (e: Exception) {
            Log.d("app", "failed to update episode information")

        }
    }

    // companion object is necessary for adding parameters to the View Model constructor
    companion object {
        // Factory for creating instances of DataViewModel
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WatchlistApplication)
                val mediaRepository = application.container.mediaRepository
                val storageRepository = application.container.storageRepository
                DataViewModel(mediaRepository, storageRepository)
            }
        }
    }

}