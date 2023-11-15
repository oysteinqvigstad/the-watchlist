package com.example.thewatchlist.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.Movie
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.screens.DetailScreen
import com.example.thewatchlist.ui.screens.MovieScreen
import com.example.thewatchlist.ui.screens.SearchScreen
import com.example.thewatchlist.ui.screens.SettingsScreen
import com.example.thewatchlist.ui.screens.ShowScreen
import com.example.thewatchlist.ui.theme.KashmirBlue

/**
 * Composable function that represents the main navigation controller for the app.
 *
 * @param navController The navigation controller for managing navigation within the app.
 * @param mainNavState The ViewModel responsible for managing the state of the main navigation.
 * @param dataViewModel The ViewModel responsible for managing data related to the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavController(
    navController: NavHostController = rememberNavController(),
    mainNavState: MainNavState = viewModel(),
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory)
) {
    val context = LocalContext.current
    // Get the active menu item from the mainNavState ViewModel
    val activeMenuItem = mainNavState.activeMainNavItem
    val moviesNotify = dataViewModel.mediaList.find { it is Movie && it.notify } != null
    val showsNotify = dataViewModel.mediaList.find { it is TV && it.notify } != null


    // Create a Scaffold with a bottom and top navigation bar
    Scaffold(
        bottomBar =  {
            NavigationBar {
                // set the appropriate top bar options
                mainNavState.mainNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.mainNavOption == MainNavOption.Movies && moviesNotify ||
                                        item.mainNavOption == MainNavOption.Shows && showsNotify) {
                                        Box(modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.Red, shape = CircleShape))
                                    }
                                },
                                content = {
                                        Image(
                                            painter = painterResource(id = mainNavState.getImageId(item)),
                                            contentDescription = item.mainNavOption.toString(),
                                            modifier = Modifier.size(24.dp)
                                        )
                                }
                            )
                        },
                        label = { Text(item.mainNavOption.toString()) },
                        selected = activeMenuItem == item.mainNavOption,
                        onClick = {
                            // Set the active main navigation item and navigate
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
            // Define composable destinations for various main navigation options
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
                    dataViewModel = dataViewModel,
                    media = dataViewModel.detailsMediaItem!!
                )
            }
        }
    }

}

/**
 * Composable function that represents a tab navigation bar.
 *
 * @param activeTopNavOption The currently active top navigation option.
 * @param topNavItems A list of top navigation options to display as tabs.
 * @param onClick A callback function to handle tab click events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabNavigation(
    activeTopNavOption: TopNavOption = TopNavOption.ToWatch,
    topNavItems: List<TopNavOption>,
    mediaItems: List<Media>,
    onClick: (TopNavOption) -> Unit = {}
) {
    // Create a secondary tab row for displaying the top navigation options
    SecondaryTabRow(
        selectedTabIndex = topNavItems.indexOf(activeTopNavOption),
        contentColor = KashmirBlue

    ) {
        // Iterate through the top navigation items to create individual tabs
        topNavItems.forEach { type ->
            val notify = mediaItems.find { it.status == type && it.notify } != null
            val count = mediaItems.count { it.status == type }
            Tab(
                text = {
                    Row(
                        verticalAlignment = CenterVertically
                    ) {
                        Text(text = type.toString())
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                        Badge(
                            containerColor = if (notify) Color.Red else Color.LightGray,
                            contentColor = Color.White
                        ){
                            Text(text = " $count ")
                        }
                    }
               },
                selected = type == activeTopNavOption,
                onClick = { onClick(type) },
            )
        }
    }
}

/**
 * Allows a user to share an active list in text form with the use of Android Share Intent
 *
 * @param context Context from which the share is initiated.
 * @param textToShare Text to be shared.
 */
fun shareContent(context: Context, textToShare: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, textToShare)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, null))
}