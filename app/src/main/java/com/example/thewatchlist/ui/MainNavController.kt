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
import com.example.thewatchlist.data.MainNavOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavController(
    navController: NavHostController = rememberNavController(),
    viewModel: MainNavState = viewModel()
) {
    val activeMenuItem = viewModel.activeMenuItem

    Scaffold(
        bottomBar =  {
            NavigationBar {
                viewModel.navBarItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Image(
                                painter = painterResource(id = viewModel.getImageId(item)),
                                contentDescription = item.mainNavOption.toString(),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(item.mainNavOption.toString()) },
                        selected = activeMenuItem == item.mainNavOption,
                        onClick = {
                            viewModel.setMenuItem(item.mainNavOption)
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
            startDestination = viewModel.activeMenuItem.toString(),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainNavOption.Search.toString()) {
                SearchScreen()
            }
            composable(MainNavOption.Movies.toString()) {
                MediaScreen(mainNavOption = MainNavOption.Movies)
            }
            composable(MainNavOption.Shows.toString()) {
                MediaScreen(mainNavOption = MainNavOption.Shows)
            }
            composable(MainNavOption.Settings.toString()) {
                SettingsScreen()
            }
        }
    }

}
