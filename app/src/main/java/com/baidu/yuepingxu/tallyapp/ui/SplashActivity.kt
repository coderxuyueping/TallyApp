package com.baidu.yuepingxu.tallyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.util.SpSet
import com.baidu.yuepingxu.tallyapp.util.Spget

class SplashActivity : AppCompatActivity() {

    private var viewPage: ViewPager? = null
    private var dumpBtn: Button? = null
    private var list: Array<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewPage = findViewById(R.id.view_page)
        dumpBtn = findViewById(R.id.btn_dump)

        initData()
        viewPage?.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return list?.size ?: 0
            }

            override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
                container.removeView(view as View?)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {

                val imageView = ImageView(this@SplashActivity)
                imageView.setImageResource(list!![position])
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(imageView)
                return imageView
            }

        }

        // 添加监听
        viewPage?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == list!!.size - 1) {
                    dumpBtn?.visibility = View.VISIBLE
                    viewPage?.postDelayed({ dump() }, 3000)
                }
            }

        })

        dumpBtn?.setOnClickListener { _ ->
            dump()
        }
    }

    private fun initData() {
        if (this.Spget("first_enter")) {
            list = arrayOf(
                R.drawable.c,
                R.drawable.d,
                R.drawable.f
            )
        } else {
            list = arrayOf(
                R.drawable.g
            )

            dumpBtn?.visibility = View.VISIBLE
            viewPage?.postDelayed({ dump() }, 3000)
        }
    }

    private fun dump() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        SpSet("first_enter", false)
        finish()
    }

    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPage?.handler?.removeCallbacksAndMessages(null)
    }
}
