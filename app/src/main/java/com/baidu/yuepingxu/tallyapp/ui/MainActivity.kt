package com.baidu.yuepingxu.tallyapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.baidu.yuepingxu.tallyapp.R
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class MainActivity : AppCompatActivity() {

    private var tallyFragment: Fragment? = null
    private var lookFragment: Fragment? = null

    private var lastTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tallyFragment = TallyFragment()
        lookFragment = LookFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment, tallyFragment!!)
            .commitAllowingStateLoss()
        supportFragmentManager.beginTransaction().add(R.id.fragment, lookFragment!!)
            .commitAllowingStateLoss()

        setImgTab(true)

        tally_layout.setOnClickListener {
            setImgTab(true)
        }

        look_layout.setOnClickListener {
            setImgTab(false)
        }

    }

    private fun setImgTab(selectTally: Boolean) {
        tally_iv.isSelected = selectTally
        look_iv.isSelected = !selectTally
        changeFragment(selectTally)
    }

    private fun changeFragment(selectTally: Boolean) {
        if (selectTally) {
            supportFragmentManager.beginTransaction().hide(lookFragment!!).show(tallyFragment!!)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().hide(tallyFragment!!).show(lookFragment!!)
                .commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTime > 2000){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            lastTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}