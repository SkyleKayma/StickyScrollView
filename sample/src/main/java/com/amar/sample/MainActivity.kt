package com.amar.sample

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var redShoeVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buy, R.id.save ->
                Toast.makeText(
                    this,
                    if (scrollView.isFooterSticky) "Footer is Sticky" else "Footer is not sticky",
                    Toast.LENGTH_SHORT
                ).show()
            R.id.title ->
                Toast.makeText(
                    this,
                    if (scrollView.isHeaderSticky) "Header is Sticky" else "Header is not sticky",
                    Toast.LENGTH_SHORT
                ).show()
            R.id.other_product -> switchShoeViews()
        }
    }

    private fun switchShoeViews() {
        if (redShoeVisible) {
            redShoeVisible = false
            main_shoe_picture.setImageResource(R.drawable.similar_1)
            (main_shoe_picture.layoutParams as LinearLayout.LayoutParams).apply {
                height = dpToPixel(320).toInt()
            }
        } else {
            redShoeVisible = true
            main_shoe_picture.setImageResource(R.drawable.nike)
            (main_shoe_picture.layoutParams as LinearLayout.LayoutParams).apply {
                height = dpToPixel(420).toInt()
            }
        }
    }

    private fun dpToPixel(pixel: Int): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel.toFloat(), resources.displayMetrics)
}