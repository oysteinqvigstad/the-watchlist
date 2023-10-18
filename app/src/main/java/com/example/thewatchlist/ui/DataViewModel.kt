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
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.network.SearchStatus
import info.movito.themoviedbapi.model.tv.TvEpisode
import kotlinx.coroutines.launch
import org.apache.commons.lang3.ObjectUtils.Null
import java.io.IOException


class DataViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
    var searchStatus: SearchStatus by mutableStateOf(SearchStatus.NoAction)
        private set

    var detailsMediaItem: Media? by mutableStateOf(null)
        private set

    var mediaList: SnapshotStateList<Media> = mutableStateListOf()
        private set

    var searchResults: SnapshotStateList<Media>? = mutableStateListOf()

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

    fun setEpisodeCheckmark(checked: Boolean, tv: TV, episode: Episode) {
        val updatedTv = tv.copy(seasonsNew = tv.seasonsNew.map { season ->
            season.copy(episodes = season.episodes.map {
                if (it == episode) episode.copy(seen = checked) else it
            })
        })
        updateMediaEntry(updatedTv)
    }

    private fun updateMediaEntry(media: Media) {
        // updating media list in watchlist
        var index = mediaList.indexOfFirst { media.id == it.id }
        if (index >= 0) {
            mediaList[index] = media
        }
        // updating media in detail screen
        if (detailsMediaItem?.id == media.id) {
            detailsMediaItem = media
        }

        searchResults?.indexOfFirst { media.id == it.id }?.let { index ->
            searchResults!![index] = media

        }
    }

    fun setActiveDetailsMediaItem(media: Media) {
        detailsMediaItem = media
    }

    fun isInWatchlist(media: Media): Boolean {
        return mediaList.find { media.id == it.id && it.status != TopNavOption.History } != null
    }

    fun moveMediaTo(media: Media, tab: TopNavOption?) {
        mediaList.remove(media)
        if (tab != null) {
            media.status = tab
            mediaList.add(media)
        }
    }

    suspend fun updateEpisodesNew(tv: TV) {
        try {
            val newTv = tv.copy(seasonsNew = tv.seasonsNew.map { season ->
                mediaRepository.getEpisodesNew(tv.id, season.seasonNumber)!!.let { episodes ->
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WatchlistApplication)
                val mediaRepository = application.container.mediaRepository
                DataViewModel(mediaRepository)
            }
        }
    }

}