package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.thewatchlist.R
import com.example.thewatchlist.data.navigation.MainNavItem
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption

/**
 * ViewModel class that manages the navigation state of the application.
 */
class MainNavState : ViewModel() {
    // Properties to keep track of active navigation items
    var activeMainNavItem by mutableStateOf(MainNavOption.Movies)
    var activeMovieNavItem by mutableStateOf(TopNavOption.ToWatch)
    var activeShowNavItem by mutableStateOf(TopNavOption.Watching)

    // Lists of navigation items and icons
    var mainNavItems = listOf(
        MainNavItem(MainNavOption.Search, R.drawable.ic_search_active, R.drawable.ic_search, "search"),
        MainNavItem(MainNavOption.Movies, R.drawable.ic_film_active, R.drawable.ic_film, "movies"),
        MainNavItem(MainNavOption.Shows, R.drawable.ic_shows_active, R.drawable.ic_shows, "shows"),
        MainNavItem(MainNavOption.Settings, R.drawable.ic_settings_active, R.drawable.ic_settings, "settings"),
    )
    var topNavShowsItems = listOf(
        TopNavOption.ToWatch,
        TopNavOption.Watching,
        TopNavOption.History
    )

    var topNavMovieItems = listOf(
        TopNavOption.ToWatch,
        TopNavOption.History
    )

    /**
     * Function to set the active main navigation item.
     * @param mainNavOption The selected main navigation item.
     */
    fun setMainNavItem(mainNavOption: MainNavOption) {
        activeMainNavItem = mainNavOption
    }

    /**
     * Function to set the active movie navigation item.
     * @param navOption The selected movie navigation item.
     */
    fun setMovieNavItem(navOption: TopNavOption) {
        activeMovieNavItem = navOption
    }

    /**
     * Function to set the active show navigation item.
     * @param navOption The selected show navigation item.
     */
    fun setShowNavItem(navOption: TopNavOption) {
        activeShowNavItem = navOption
    }

    /**
     * Function to get the appropriate icon for a menu item.
     * @param menuItem The menu item to retrieve the icon for.
     * @return The icon resource ID based on the active main navigation item.
     */
    fun getImageId(menuItem: MainNavItem): Int {
        return if (menuItem.mainNavOption == activeMainNavItem) menuItem.icon else menuItem.icon2
    }
}