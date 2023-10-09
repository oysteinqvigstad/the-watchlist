package com.example.thewatchlist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thewatchlist.network.SearchStatus
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.components.Banner

@Composable
fun SearchScreen(dataViewModel: DataViewModel) {

    LaunchedEffect(Unit) {
        dataViewModel.searchTmdb("avatar")
    }

    Column {
        Text(text = "online søk for 'avatar':")
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        when (val res = dataViewModel.searchStatus) {
            is SearchStatus.Success -> { res.results.forEach { Banner(item = it) } }
            is SearchStatus.Loading -> Loading()
            is SearchStatus.Error -> Error()
        }

    }
}

@Composable
fun Loading() {
    Text(text = "Loading...")
}

@Composable
fun Error() {
    // TODO: handle better error messages
    Text(text = "something went wrong")
}
