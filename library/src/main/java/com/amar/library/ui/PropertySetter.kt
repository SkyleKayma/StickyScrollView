package com.amar.library.ui

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat

/**
 * Created by Amar Jain on 28/03/17.
 */
object PropertySetter {

    @JvmStatic
    fun setTranslationZ(view: View, translationZ: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTranslationZ(view, translationZ)
        } else if (translationZ != 0f) {
            view.bringToFront()
            (view.parent as? View)?.invalidate()
        }
    }
}