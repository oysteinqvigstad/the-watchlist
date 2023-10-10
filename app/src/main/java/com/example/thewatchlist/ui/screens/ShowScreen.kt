package com.example.thewatchlist.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.MainNavState
import com.example.thewatchlist.ui.TabNavigation
import com.example.thewatchlist.ui.components.Banner

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScreen(
    mainNavState: MainNavState,
    title: String,
    dataViewModel: DataViewModel
) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(title) }
        )

        TabNavigation(
            activeTopNavOption = mainNavState.activeShowNavItem,
            topNavItems = mainNavState.topNavShowsItems,
            onClick = { mainNavState.setShowNavItem(it) })

        dataViewModel.mediaList.forEach {
            if (it is TV && it.status == mainNavState.activeShowNavItem) {
                Banner(
                    item = it,
                    activeBottomNav = MainNavOption.Movies,
                    onClick = { dataViewModel.addMediaToList(it) }
                )
            }

        }

    }
}