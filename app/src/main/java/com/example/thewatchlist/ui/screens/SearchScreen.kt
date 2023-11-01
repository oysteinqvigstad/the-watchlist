package com.example.thewatchlist.ui.screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.thewatchlist.R
import com.example.thewatchlist.data.SearchStatus
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.Banner

/**
 * Composable function representing the search screen.
 *
 * @param dataViewModel The ViewModel responsible for managing data related to the app.
 * @param mainNavController The navigation controller for the main navigation.
 */
@Composable
fun SearchScreen(
    dataViewModel: DataViewModel,
    mainNavController: NavController
) {
    // Define a mutable state for search text
    var searchText: String? by remember { mutableStateOf(null) }

    // Create a box layout to contain the search field and search results
    Box {
        SearchField(searchValue = {searchText = it})
        SearchResults(
            dataViewModel = dataViewModel,
            mainNavController = mainNavController,
            searchText = searchText
        )

    }
}

/**
 * Composable function representing the search field.
 *
 * @param searchValue A callback function for handling search value changes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchValue: (String) -> Unit
) {
    // Define mutable state for the search text and the active state
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // Create a search bar with various properties and icons
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = { text = it },
        onSearch = {
            searchValue(text)
            active = false
        },
        shadowElevation = 10.dp,
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

/**
 * Composable function responsible for displaying search results.
 *
 * @param dataViewModel The ViewModel responsible for managing data related to the app.
 * @param mainNavController The navigation controller for the main navigation.
 * @param searchText The text to search for.
 */
@Composable
fun SearchResults(
    dataViewModel: DataViewModel,
    mainNavController: NavController,
    searchText: String?,


) {
    // Use LaunchedEffect to trigger the search when searchText changes
    LaunchedEffect(searchText) {
        searchText?.let {
            dataViewModel.searchTmdb(it)
        }
    }

    // Create a LazyColumn for displaying search results
    LazyColumn {
        item { Spacer(modifier = Modifier.padding(top = 70.dp)) }
        item {
            when (dataViewModel.searchStatus) {
                is SearchStatus.Success -> {
                    // Display search results as banners
                    dataViewModel.searchResults?.forEach {
                        Banner(
                            media = it,
                            activeBottomNav = MainNavOption.Search,
                            onDetails = {
                                dataViewModel.setActiveDetailsMediaItem(it)
                                mainNavController.navigate("details")
                            },
                            dataViewModel = dataViewModel

                        )
                    }
                }

                is SearchStatus.Loading -> LoadingIndicator()
                is SearchStatus.Error -> ErrorMessage()
                is SearchStatus.NoAction -> {}
            }
        }
    }
}


/**
 * Composable function to display a loading indicator.
 */
@Composable
fun LoadingIndicator() {
    // Set up an image loader and load a loading GIF
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val gifPainter = rememberAsyncImagePainter( model = ImageRequest.Builder(LocalContext.current)
        .data(data = R.drawable.frame_9__3_)
        .size(Size.ORIGINAL)
        .build(),
        imageLoader = imageLoader)

    Image(
        painter = gifPainter,
        contentDescription = "Loading gif",
        modifier = Modifier
            .fillMaxWidth()
    )
}

/**
 * Composable function to display an error message.
 */
@Composable
fun ErrorMessage() {
//     TODO: handle better error messages
    Text(
        text = "Something went wrong when trying to fetch the data." +
                "Please check your network connection or try again later.",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
        )
}