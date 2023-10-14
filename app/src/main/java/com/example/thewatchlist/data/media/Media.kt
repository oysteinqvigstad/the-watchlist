package com.example.thewatchlist.data.media

import android.util.Log
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color
import com.example.thewatchlist.data.navigation.MainNavOption
import com.example.thewatchlist.data.navigation.TopNavOption
import com.example.thewatchlist.ui.DataViewModel
import com.example.thewatchlist.ui.theme.KashmirBlue
import com.example.thewatchlist.ui.theme.RemoveColor

data class Button(
    val label: String,
    val action: (Media) -> Unit,
    val color: Color
)


open class Media(val id: Int) : Cloneable {
    var status = TopNavOption.ToWatch

    public override fun clone(): Media {
        return super.clone() as Media
    }

    open fun getPrimaryAction(
        activeNavOption: MainNavOption,
        dataViewModel: DataViewModel
    ): Button {
        return if (dataViewModel.isInWatchlist(this)) {
            Button(
                label = "Remove from Watchlist",
                action = { dataViewModel.removeMediaFromList(this) },
                color = RemoveColor)
        } else {
            Button(
                label = "Add to Watchlist",
                action = { dataViewModel.addMediaToList(this) },
                color = KashmirBlue)
        }
    }
}