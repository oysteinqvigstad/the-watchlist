package com.example.thewatchlist.ui.screens

import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.thewatchlist.R
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

    Box {
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



@Composable
fun SearchResults(
    dataViewModel: DataViewModel,
    mainNavController: NavController,
    searchText: String?,


) {
    LaunchedEffect(searchText) {
        searchText?.let {
            dataViewModel.searchTmdb(it)
        }
    }

    LazyColumn {
        item { Spacer(modifier = Modifier.padding(top = 70.dp)) }
        item {
            when (val res = dataViewModel.searchStatus) {
                is SearchStatus.Success -> {
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



@Composable
fun LoadingIndicator() {

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

@Composable
fun ErrorMessage() {
//     TODO: handle better error messages
    Text(text = "something went wrong")
}