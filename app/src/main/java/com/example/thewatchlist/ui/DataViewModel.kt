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
import com.example.thewatchlist.data.MediaRepository
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Season
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.network.SearchStatus
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * ViewModel class responsible for managing data and interactions with the media repository.
 *
 * @param mediaRepository The repository used for retrieving and updating media data.
 */
class DataViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
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
                SearchStatus.Success
            } catch (e: IOException) {
                SearchStatus.Error
            } catch (e: NullPointerException) {
                SearchStatus.Error
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
        searchResults?.indexOfFirst { media.id == it.id }?.let { index ->
            searchResults!![index] = media

        }
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
            Log.d("me", "Ooops")
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
            Log.d("me", "Ooops")

        }


    }

    // companion object is necessary for adding parameters to the View Model constructor
    companion object {
        // Factory for creating instances of DataViewModel
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WatchlistApplication)
                val mediaRepository = application.container.mediaRepository
                DataViewModel(mediaRepository)
            }
        }
    }

}