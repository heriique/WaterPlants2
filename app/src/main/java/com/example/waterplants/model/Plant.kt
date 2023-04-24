package com.example.waterplants.model

import android.media.Image

data class Plant(var pin: Int?, var intervalDays: Int?, var amount: Int?, var hourOfDay: Int?,
                 var watered: Boolean?, var name: String?, var image: Image?) {
    override fun toString(): String {
        return if (name == null)
            ""
        else "$name"
    }
}