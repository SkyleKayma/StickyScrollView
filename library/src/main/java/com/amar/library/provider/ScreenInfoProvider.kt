package com.amar.library.provider

import android.content.Context
import android.graphics.Point
import com.amar.library.provider.interfaces.IScreenInfoProvider

/**
 * Created by Amar Jain on 17/03/17.
 */
class ScreenInfoProvider(private val mContext: Context) : IScreenInfoProvider {
    override val screenHeight: Int
        get() = deviceDimension.y

    override val screenWidth: Int
        get() = deviceDimension.x

    private val deviceDimension: Point
        get() {
            val lPoint = Point()
            val metrics = mContext.resources.displayMetrics
            lPoint.x = metrics.widthPixels
            lPoint.y = metrics.heightPixels
            return lPoint
        }
}