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
import com.example.thewatchlist.data.media.Movie
import com.example.thewatchlist.data.media.TV
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.DetailedInfo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    dataViewModel: DataViewModel
) {
    // TODO: Handle redirection if null
    val media = dataViewModel.detailsMediaItem!!
    val title = media.let {
        when (it) {
            is Movie -> (it as Movie).title
            is TV -> (it as TV).title
            else -> ""
        }
    }

    LaunchedEffect(Unit) {
        if (media is TV) {
            dataViewModel.updateEpisodes(media)
        }
    }



    Column {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text(text = title ?: "") }
        )
        // TODO: handle redirect if null
        DetailedInfo(media = media, dataViewModel = dataViewModel)
    }
}
