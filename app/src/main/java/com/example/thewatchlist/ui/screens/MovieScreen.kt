package com.example.thewatchlist.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.thewatchlist.ui.MainNavState
import com.example.thewatchlist.ui.TabNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    mainNavState: MainNavState,
    mainNavController: NavController,
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
        
        Button(
            onClick = { mainNavController.navigate("details/test 1-2-3") }
        ) {
          Text(text = "Navigating to details screen!")
        }
        
    }
    
    
}
