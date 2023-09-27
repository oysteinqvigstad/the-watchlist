package com.example.thewatchlist.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.TopNavItem
import com.example.thewatchlist.data.TopNavOption
import com.example.thewatchlist.ui.theme.KashmirBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    navState: NavState,
    title: String
) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(title) }
        )

        TabNavigation(
            activeTopNavOption = navState.activeTopNavItem,
            topNavItems = navState.topNavItems,
            onClick = { navState.setTopNavItem(it) })

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