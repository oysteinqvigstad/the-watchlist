package com.example.thewatchlist.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.thewatchlist.data.MainNavOption

@Composable
fun MediaScreen(
    mainNavOption: MainNavOption
) {
    Text(mainNavOption.toString() + " Screen")
}