package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.network.Tmdb
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.tv.TvSeries
import kotlinx.coroutines.launch
import java.io.IOException


class DataViewModel : ViewModel() {
    var searchStatus: SearchStatus by mutableStateOf(SearchStatus.Loading)
        private set

    suspend fun searchTmdb(title: String) {
        searchStatus = SearchStatus.Loading
        viewModelScope.launch {
            searchStatus = try {
                val results = Tmdb.searchMulti(title)?.mapNotNull {
                    when (it) {
                        is MovieDb -> Movie(it)
                        is TvSeries -> TV(it)
                        else -> null
                    }
                }
                SearchStatus.Success(results = results!!)
            } catch (e: IOException) {
                SearchStatus.Error
            } catch (e: NullPointerException) {
                SearchStatus.Error
            }
        }
    }
}