package com.amar.library.provider.interfaces

import androidx.annotation.StyleableRes

/**
 * Created by Amar Jain on 17/03/17.
 */
interface IResourceProvider {
    fun getResourceId(@StyleableRes styleResId: Int): Int
    fun recycle()
}