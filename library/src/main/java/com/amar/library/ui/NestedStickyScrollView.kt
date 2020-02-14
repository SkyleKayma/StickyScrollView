package com.amar.library.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import com.amar.library.R
import com.amar.library.provider.ResourceProvider
import com.amar.library.provider.ScreenInfoProvider
import com.amar.library.provider.interfaces.IResourceProvider
import com.amar.library.provider.interfaces.IScreenInfoProvider
import com.amar.library.ui.PropertySetter.setTranslationZ
import com.amar.library.ui.interfaces.IScrollViewListener
import com.amar.library.ui.presentation.IStickyScrollPresentation
import com.amar.library.ui.presenter.StickyScrollPresenter

class NestedStickyScrollView(context: Context, attrs: AttributeSet? = null) : NestedScrollView(context, attrs),
    IStickyScrollPresentation {

    private var scrollViewListener: IScrollViewListener? = null
    private var stickyFooterView: View? = null
    private var stickyHeaderView: View? = null
    private var mStickyScrollPresenter: StickyScrollPresenter

    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    init {
        val screenInfoProvider: IScreenInfoProvider = ScreenInfoProvider(context)
        val resourceProvider: IResourceProvider = ResourceProvider(context, attrs, R.styleable.StickyScrollView)
        mStickyScrollPresenter = StickyScrollPresenter(this, screenInfoProvider, resourceProvider)

        onGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mStickyScrollPresenter.onGlobalLayoutChange(
                    R.styleable.StickyScrollView_stickyHeader,
                    R.styleable.StickyScrollView_stickyFooter
                )
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        stickyFooterView?.let {
            if (!changed) {
                mStickyScrollPresenter.recomputeFooterLocation(getRelativeTop(it))
            }
        }

        stickyHeaderView?.also {
            mStickyScrollPresenter.recomputeHeaderLocation(it.top)
        }
    }

    private fun getRelativeTop(myView: View): Int =
        if (myView.parent == myView.rootView) myView.top else myView.top + getRelativeTop(myView.parent as View)

    override fun initHeaderView(id: Int) {
        stickyHeaderView = findViewById(id)

        stickyHeaderView?.let {
            mStickyScrollPresenter.initStickyHeader(it.top)
        }
    }

    override fun initFooterView(id: Int) {
        stickyFooterView = findViewById(id)

        stickyFooterView?.let {
            mStickyScrollPresenter.initStickyFooter(it.measuredHeight, getRelativeTop(it))
        }
    }

    override fun freeHeader() {
        stickyHeaderView?.also {
            it.translationY = 0f
            setTranslationZ(it, 0f)
        }
    }

    override fun freeFooter() {
        stickyFooterView?.translationY = 0f
    }

    override fun stickHeader(translationY: Int) {
        stickyHeaderView?.also {
            it.translationY = translationY.toFloat()
            setTranslationZ(it, 1f)
        }
    }

    override fun stickFooter(translationY: Int) {
        stickyFooterView?.translationY = translationY.toFloat()
    }

    override val currentScrollYPos: Int
        get() = scrollY

    override fun onScrollChanged(mScrollX: Int, mScrollY: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(mScrollX, mScrollY, oldX, oldY)
        mStickyScrollPresenter.onScroll(mScrollY)
        scrollViewListener?.onScrollChanged(mScrollX, mScrollY, oldX, oldY)
    }

    val isFooterSticky: Boolean = mStickyScrollPresenter.isFooterSticky

    val isHeaderSticky: Boolean = mStickyScrollPresenter.isHeaderSticky

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        scrollViewListener?.onScrollStopped(clampedY)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(SUPER_STATE, super.onSaveInstanceState())
            putBoolean(SCROLL_STATE, mStickyScrollPresenter.mScrolled)
        }
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var bundle: Parcelable? = null
        if (state is Bundle) {
            mStickyScrollPresenter.mScrolled = state.getBoolean(SCROLL_STATE)
            bundle = state.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(bundle)
    }

    companion object {
        private const val SCROLL_STATE = "scroll_state"
        private const val SUPER_STATE = "super_state"
    }
}