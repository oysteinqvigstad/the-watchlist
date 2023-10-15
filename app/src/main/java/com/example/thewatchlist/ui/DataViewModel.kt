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
import com.example.thewatchlist.data.media.Media
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.network.SearchStatus
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
        viewModelScope.async { tv.updateEpisodes() }.await()
        updateMediaEntry(tv)

    }


    fun addMediaToList(media: Media) {
        val existingMedia = mediaList.find { media.id == it.id }
        if (existingMedia != null) {
            existingMedia.status = TopNavOption.ToWatch
            updateMediaEntry(existingMedia)
        } else {
            media.status = TopNavOption.ToWatch
            mediaList.add(media)
        }
    }

    fun removeMediaFromList(media: Media) {
        mediaList.remove(media)
    }

    fun moveToHistory(media: Media) {
        media.status = TopNavOption.History
        updateMediaEntry(media)
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

    fun updateMediaEntry(media: Media) {
        val index = mediaList.indexOf(media)
        if (index >= 0) {
            mediaList[index] = media.clone()
        }
        if (detailsMediaItem?.id == media.id) {
            detailsMediaItem = media.clone()
        }
    }

    fun setActiveDetailsMediaItem(media: Media) {
        detailsMediaItem = media
    }

    fun isInWatchlist(media: Media): Boolean {
        return mediaList.find { media.id == it.id && it.status != TopNavOption.History } != null
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