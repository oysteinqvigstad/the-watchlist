package com.example.thewatchlist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.TopNavItem
import com.example.thewatchlist.data.TopNavOption
import com.example.thewatchlist.ui.theme.KashmirBlue

@Composable
fun MediaScreen(
    navState: NavState
) {
    Column {
        TabNavigation(
            activeTopNavOption = navState.activeTopNavItem,
            topNavItems = navState.topNavItems,
            onClick = { navState.setTopNavItem(it) }
        )
        Text(navState.activeMainNavItem.toString() + " Screen")
    }
}

@Composable
fun TabNavigation(
    activeTopNavOption: TopNavOption = TopNavOption.ToWatch,
    topNavItems: List<TopNavItem>,
    onClick: (TopNavOption) -> Unit = {}
) {
    TabRow(
        selectedTabIndex = activeTopNavOption.ordinal,
        contentColor = KashmirBlue

    ) {
        topNavItems.forEach { (type, label) ->
            Tab(
                text = { Text(text = label) },
                selected = type == activeTopNavOption,
                onClick = { onClick(type) }
            )
        }
    }
}