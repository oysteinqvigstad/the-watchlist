package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.network.Tmdb
import kotlinx.coroutines.launch
import java.io.IOException


class DataViewModel : ViewModel() {
    var searchStatus: SearchStatus by mutableStateOf(SearchStatus.Loading)
        private set

    init {
    }

    suspend fun searchTmdb(title: String) {
        viewModelScope.launch {
            searchStatus = try {
                val results = Tmdb.searchMulti(title)
                SearchStatus.Success(results = results!!)
            } catch (e: IOException) {
                SearchStatus.Error
            } catch (e: NullPointerException) {
                SearchStatus.Error
            }
        }
    }
}