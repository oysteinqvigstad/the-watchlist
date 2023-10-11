package com.example.thewatchlist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.Banner


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    dataViewModel: DataViewModel,
    mainNavController: NavController
) {

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
//    var search by remember { mutableStateOf(false) }    // activate search


    Column {

        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = { text = it },
            onSearch = {

                // Need to search for movie/series here
//                search = true


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
                if(active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if(text.isNotEmpty()) {
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

/*
        // Searches once with the input,
        // but doesn't run again if we want to search for something else
        if(search) { LaunchedEffect(Unit) { dataViewModel.searchTmdb(text) } }
*/


        // Searches only once when the app first starts, therefore searches for nothing
        LaunchedEffect(Unit) {
            dataViewModel.searchTmdb(text)
        }

        Text(text = "online søk for '$text':")
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        when (val res = dataViewModel.searchStatus) {
            is SearchStatus.Success -> { res.results.forEach {
                Banner(
                    media = it,
                    activeBottomNav = MainNavOption.Search,
                    onDetails = {
                        dataViewModel.setActiveDetailsMediaItem(it)
                        mainNavController.navigate("details")
                    },
                    onAdd = { dataViewModel.addMediaToList(it) }
                )
            } }
            is SearchStatus.Loading -> Loading()
            is SearchStatus.Error -> Error()
        }

    }
}

@Composable
fun Loading() {
    Text(text = "Loading...")
}

@Composable
fun Error() {
    // TODO: handle better error messages
    Text(text = "something went wrong")
}