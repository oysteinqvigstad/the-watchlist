package com.example.thewatchlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.thewatchlist.data.persistence.StorageDb
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.MainNavController
import com.example.thewatchlist.ui.theme.TheWatchlistTheme

/**
 * This is the main activity for the application.
 * It sets up the application's user interface and initializes the navigation system.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            StorageDb::class.java, "database-name"
        ).build()

        val dataViewModel: DataViewModel by viewModels(factoryProducer = { DataViewModel.Factory})
        dataViewModel.setStorageDb(db)

        setContent {
            TheWatchlistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainNavController()
                }
            }
        }
    }
}