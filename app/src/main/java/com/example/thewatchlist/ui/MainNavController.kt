package com.example.thewatchlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.screens.DetailScreen
import com.example.thewatchlist.ui.screens.MovieScreen
import com.example.thewatchlist.ui.screens.SearchScreen
import com.example.thewatchlist.ui.screens.SettingsScreen
import com.example.thewatchlist.ui.screens.ShowScreen
import com.example.thewatchlist.ui.theme.KashmirBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavController(
    navController: NavHostController = rememberNavController(),
    mainNavState: MainNavState = viewModel(),
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory)
) {
    val activeMenuItem = mainNavState.activeMainNavItem

    Scaffold(
        bottomBar =  {
            NavigationBar {
                mainNavState.mainNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Image(
                                painter = painterResource(id = mainNavState.getImageId(item)),
                                contentDescription = item.mainNavOption.toString(),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(item.mainNavOption.toString()) },
                        selected = activeMenuItem == item.mainNavOption,
                        onClick = {
                            mainNavState.setMainNavItem(item.mainNavOption)
                            navController.navigate(item.mainNavOption.toString())
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // The navHost draws the composable that corresponds with navController.navigate()
        NavHost(
            navController = navController,
            startDestination = mainNavState.activeMainNavItem.toString(),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainNavOption.Search.toString()) {
                SearchScreen(
                    dataViewModel = dataViewModel,
                    mainNavController = navController
                )
            }
            composable(MainNavOption.Movies.toString()) {
                MovieScreen(
                    mainNavState = mainNavState,
                    title = MainNavOption.Movies.toString(),
                    mainNavController = navController,
                    dataViewModel = dataViewModel
                )

            }
            composable(MainNavOption.Shows.toString()) {
                ShowScreen(
                    mainNavState = mainNavState,
                    title = MainNavOption.Shows.toString(),
                    mainNavController = navController,
                    dataViewModel = dataViewModel,
                )
            }
            composable(MainNavOption.Settings.toString()) {
                SettingsScreen(title = MainNavOption.Settings.toString())
            }
            composable("details") {
                DetailScreen(
                    navController = navController,
                    dataViewModel = dataViewModel
                )
            }
        }
    }

}

@Composable
fun TabNavigation(
    activeTopNavOption: TopNavOption = TopNavOption.ToWatch,
    topNavItems: List<TopNavOption>,
    onClick: (TopNavOption) -> Unit = {}
) {
    SecondaryTabRow(
        selectedTabIndex = topNavItems.indexOf(activeTopNavOption),
        contentColor = KashmirBlue

    ) {
        topNavItems.forEach { type ->
            Tab(
                text = { Text(text = type.toString()) },
                selected = type == activeTopNavOption,
                onClick = { onClick(type) }
            )
        }
    }
}
