package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.thewatchlist.R
import com.example.thewatchlist.data.MainNavItem
import com.example.thewatchlist.data.MainNavOption

/**
 * ViewModel that keeps the state of the main menu
 */
class MainNavState : ViewModel() {
    var activeMenuItem by mutableStateOf(MainNavOption.Movies)
    var navBarItems = listOf(
            MainNavItem(MainNavOption.Search, R.drawable.ic_search_active, R.drawable.ic_search, "search"),
            MainNavItem(MainNavOption.Movies, R.drawable.ic_film_active, R.drawable.ic_film, "movies"),
            MainNavItem(MainNavOption.Shows, R.drawable.ic_shows_active, R.drawable.ic_shows, "shows"),
            MainNavItem(MainNavOption.Settings, R.drawable.ic_settings_active, R.drawable.ic_settings, "settings"),
        )


    fun setMenuItem(mainNavOption: MainNavOption) {
        activeMenuItem = mainNavOption
    }
    fun getImageId(menuItem: MainNavItem): Int {
        return if (menuItem.mainNavOption == activeMenuItem) menuItem.icon else menuItem.icon2
    }
}