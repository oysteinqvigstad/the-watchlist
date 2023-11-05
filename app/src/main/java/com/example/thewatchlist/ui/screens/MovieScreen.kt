package com.example.thewatchlist.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.MainNavState
import com.example.thewatchlist.ui.TabNavigation
import com.example.thewatchlist.ui.components.Banner
import com.example.thewatchlist.ui.shareContent

/**
 * Composable function representing the movie screen.
 *
 * @param mainNavState The ViewModel responsible for managing the main navigation state.
 * @param mainNavController The navigation controller for the main navigation.
 * @param dataViewModel The ViewModel for managing data related to the app.
 * @param title The title of the screen.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    mainNavState: MainNavState,
    mainNavController: NavController,
    dataViewModel: DataViewModel,
    title: String
) {
    val context = LocalContext.current
    val mediaItems = dataViewModel.mediaList.filter {
        it is Movie && it.status == mainNavState.activeMovieNavItem
    }
    // Create a column layout to contain the top app bar, tab navigation, and movie banners
    Column {
        // Display a centered top app bar with the given title
        CenterAlignedTopAppBar(
            title = { Text(title) },
            actions = {
                IconButton(
                    onClick = {
                        shareContent(
                            context = context,
                            textToShare = mediaItems.joinToString("\n") { it.title }
                        )
                    }) {
                    Icon(Icons.Filled.Share, contentDescription = "Share")
                }
            }
        )

        // Create a tab navigation bar for switching between top navigation options
        TabNavigation(
            activeTopNavOption = mainNavState.activeMovieNavItem,
            topNavItems = mainNavState.topNavMovieItems,
            onClick = { mainNavState.setMovieNavItem(it) })

        LazyColumn {
            mediaItems.forEach {
            // Display a banner for the movie
                item {
                    Banner(
                        media = it,
                        activeBottomNav = MainNavOption.Movies,
                        onDetails = {
                            dataViewModel.setActiveDetailsMediaItem(it)
                            mainNavController.navigate("details")
                        },
                        dataViewModel = dataViewModel
                    )
                }
            }
        }
    }
}
