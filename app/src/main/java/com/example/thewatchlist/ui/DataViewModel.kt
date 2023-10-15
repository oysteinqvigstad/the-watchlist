package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.thewatchlist.WatchlistApplication
import com.example.thewatchlist.data.MediaRepository
import com.example.thewatchlist.data.media.Button
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.ui.theme.RemoveColor
import info.movito.themoviedbapi.model.tv.TvEpisode
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException


class DataViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
    var searchStatus: SearchStatus by mutableStateOf(SearchStatus.NoAction)
        private set

    var detailsMediaItem: Media? by mutableStateOf(null)
        private set

    var mediaList: SnapshotStateList<Media> = mutableStateListOf()
        private set

    suspend fun searchTmdb(title: String) {
        searchStatus = SearchStatus.Loading
        viewModelScope.launch {
            searchStatus = try {
                val results = mediaRepository.getMultiMedia(title)
                SearchStatus.Success(results = results!!)
            } catch (e: IOException) {
                SearchStatus.Error
            } catch (e: NullPointerException) {
                SearchStatus.Error
            }
        }
    }

    suspend fun updateEpisodes(tv: TV) {
        // TODO: Reimplement
//        viewModelScope.async { tv.updateEpisodes() }.await()
//        updateMediaEntry(tv)

    }


    fun setEpisodeCheckmark(checked: Boolean, episode: TvEpisode, tv: TV) {
        val seenEntry = Pair(episode.seasonNumber, episode.episodeNumber)
        if (checked) {
            tv.seenList.add(seenEntry)
        } else {
            tv.seenList.remove(seenEntry)
        }
        updateMediaEntry(tv)
    }

    private fun updateMediaEntry(media: Media) {
        val clonedMedia = when (media) {
            is TV -> media.copy()
            is Movie -> media.copy()
            else -> media
        }
        val index = mediaList.indexOf(media)
        if (index >= 0) {
            mediaList[index] = clonedMedia
        }
        if (detailsMediaItem?.id == media.id) {
            detailsMediaItem = clonedMedia
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