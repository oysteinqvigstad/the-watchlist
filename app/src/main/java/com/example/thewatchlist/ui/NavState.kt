package com.example.thewatchlist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.thewatchlist.R
import com.example.thewatchlist.data.MainNavItem
import com.example.thewatchlist.data.MainNavOption
import com.example.thewatchlist.data.TopNavItem
import com.example.thewatchlist.data.TopNavOption

/**
 * ViewModel that keeps the state of the main menu
 */
class NavState : ViewModel() {
    var activeMainNavItem by mutableStateOf(MainNavOption.Movies)
    var mainNavItems = listOf(
        MainNavItem(MainNavOption.Search, R.drawable.ic_search_active, R.drawable.ic_search, "search"),
        MainNavItem(MainNavOption.Movies, R.drawable.ic_film_active, R.drawable.ic_film, "movies"),
        MainNavItem(MainNavOption.Shows, R.drawable.ic_shows_active, R.drawable.ic_shows, "shows"),
        MainNavItem(MainNavOption.Settings, R.drawable.ic_settings_active, R.drawable.ic_settings, "settings"),
    )

    var activeTopNavItem by mutableStateOf(TopNavOption.Watching)
    var topNavItems = listOf(
        TopNavItem(TopNavOption.ToWatch, "To Watch"),
        TopNavItem(TopNavOption.Watching, "Watching"),
        TopNavItem(TopNavOption.History, "History")

    )

    fun setMainNavItem(mainNavOption: MainNavOption) {
        activeMainNavItem = mainNavOption
    }

    fun getImageId(menuItem: MainNavItem): Int {
        return if (menuItem.mainNavOption == activeMainNavItem) menuItem.icon else menuItem.icon2
    }

    fun setTopNavItem(topNavOption: TopNavOption) {
        activeTopNavItem = topNavOption
    }

}