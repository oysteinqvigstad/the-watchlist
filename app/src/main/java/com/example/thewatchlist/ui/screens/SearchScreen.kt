package com.example.thewatchlist.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.Banner



@Composable
fun SearchScreen(
    dataViewModel: DataViewModel,
    mainNavController: NavController
) {

    var searchText: String? by remember { mutableStateOf(null) }


    Column {
        SearchField(searchValue = {searchText = it})
        SearchResults(
            dataViewModel = dataViewModel,
            mainNavController = mainNavController,
            searchText = searchText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchValue: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = { text = it },
        onSearch = {
            searchValue(text)
            active = false
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        },

        ) {


    }
}



@Composable
fun SearchResults(
    dataViewModel: DataViewModel,
    mainNavController: NavController,
    searchText: String?,


    ) {
    LaunchedEffect(searchText) {
        searchText?.let {
            Log.d("me", "logged $searchText")
            dataViewModel.searchTmdb(it)
        }
    }
    when (val res = dataViewModel.searchStatus) {
        is SearchStatus.Success -> {
            LazyColumn {
                res.results.forEach {
                    item {
                        Banner(
                            media = it,
                            activeBottomNav = MainNavOption.Search,
                            onDetails = {
                                dataViewModel.setActiveDetailsMediaItem(it)
                                mainNavController.navigate("details")
                            },
                            onAdd = { dataViewModel.addMediaToList(it) }
                        )
                    }
                }

            } }
        is SearchStatus.Loading -> LoadingIndicator()
        is SearchStatus.Error -> ErrorMessage()
        is SearchStatus.Waiting -> {}
    }

}



@Composable
fun LoadingIndicator() {
    Text(text = "Loading...")
}

@Composable
fun ErrorMessage() {
//     TODO: handle better error messages
    Text(text = "something went wrong")
}