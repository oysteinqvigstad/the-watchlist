package com.example.thewatchlist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.CenterAlignedTopAppBar

/**
 * Composable function representing the settings screen.
 *
 * @param title The title of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    title: String
) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = title) }
        )
    }
}

