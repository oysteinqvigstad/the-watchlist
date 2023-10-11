package com.example.thewatchlist.data.media

import com.example.thewatchlist.data.navigation.TopNavOption

open class Media(val id: Int) : Cloneable {
    var status = TopNavOption.ToWatch

    public override fun clone(): TV {
        return super.clone() as TV
    }
}