package com.example.ui.components

import androidx.annotation.DrawableRes
import com.example.R

object PropertyImageHelper {
    @DrawableRes
    fun getDrawableForName(name: String): Int {
        return when (name) {
            "villa_luxury" -> R.drawable.villa_luxury
            "apartment_luxury" -> R.drawable.apartment_luxury
            "penthouse_living" -> R.drawable.penthouse_living
            else -> R.drawable.villa_luxury
        }
    }
}
