package com.example.thewatchlist.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.thewatchlist.data.Media
import com.example.thewatchlist.data.TV
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.DetailedInfo

/**
 * Composable function representing the detail screen for a media item.
 *
 * @param navController The navigation controller for managing screen transitions.
 * @param dataViewModel The ViewModel for managing data related to the app.
 * @param media The media item for which to display details.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
    media: Media
) {
    // Use LaunchedEffect to trigger updates for TV series if the media is of type TV
    LaunchedEffect(Unit) {
        if (media is TV) {
            dataViewModel.updateSeasonInfo(media)
            dataViewModel.updateEpisodes(media)
        }
    }

    // Create a column layout to contain the top app bar, navigation, and media details
    Column {
        // Display a centered top app bar with a back navigation icon and media title
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text(text = media.title) }
        )
        // TODO: handle redirect if null
        DetailedInfo(media = media, dataViewModel = dataViewModel)
    }
}
