package com.amar.library.ui.interfaces

interface IScrollViewListener {
    fun onScrollChanged(l: Int, t: Int, oldL: Int, oldT: Int)
    fun onScrollStopped(isStopped: Boolean)
}