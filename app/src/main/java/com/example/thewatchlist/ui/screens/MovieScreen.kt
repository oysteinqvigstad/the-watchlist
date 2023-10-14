package com.example.thewatchlist.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.MainNavState
import com.example.thewatchlist.ui.TabNavigation
import com.example.thewatchlist.ui.components.Banner

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    mainNavState: MainNavState,
    mainNavController: NavController,
    dataViewModel: DataViewModel,
    title: String
) {
    
    Column {
        CenterAlignedTopAppBar(
            title = { Text(title) }
        )

        TabNavigation(
            activeTopNavOption = mainNavState.activeMovieNavItem,
            topNavItems = mainNavState.topNavMovieItems,
            onClick = { mainNavState.setMovieNavItem(it) })
        
        dataViewModel.mediaList.forEach {
            if (it is Movie && it.status == mainNavState.activeMovieNavItem) {
                Banner(
                    media = it,
                    activeBottomNav = MainNavOption.Movies,
                    onDetails = {
                        dataViewModel.setActiveDetailsMediaItem(it)
                        mainNavController.navigate("details")
                    },
                    onAdd = { dataViewModel.addMediaToList(it) },
                    dataViewModel = dataViewModel

                )
        } }


    }
    
    
}
