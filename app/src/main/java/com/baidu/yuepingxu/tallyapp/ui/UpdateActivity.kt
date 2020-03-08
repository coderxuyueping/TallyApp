package com.baidu.yuepingxu.tallyapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.util.LogUtil
import com.baidu.yuepingxu.tallyapp.util.ObserManager
import com.baidu.yuepingxu.tallyapp.util.SqliteUtil
import kotlinx.android.synthetic.main.activity_update.buy_money
import kotlinx.android.synthetic.main.activity_update.buy_what
import kotlinx.android.synthetic.main.activity_update.save
import java.util.regex.Pattern


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class UpdateActivity : AppCompatActivity() {
    private var id: Int = 0
    private var isIncome = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        id = intent.extras?.get("id") as Int
        isIncome = intent.extras?.getBoolean("isIncome", false)!!
        LogUtil.log("id is $id")
        actionBar?.title = "更新记录"

        save.setOnClickListener {
            val buyWhat = buy_what.text.toString()
            var money = buy_money.text.toString().trim()
            if (TextUtils.isEmpty(buyWhat) && TextUtils.isEmpty(money)) {
                Toast.makeText(it.context, "必须要更新一个哦", Toast.LENGTH_SHORT).show()
            } else {
                if (isInteger(money) || isDouble(money)) {
                    if (!isIncome) {
                        money = "-$money"
                    }
                    update(buyWhat, money, id)
                } else {
                    Toast.makeText(it.context, "金额只能是整数或者小数哦", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun update(buyWhat: String, money: String, id: Int) {
        SqliteUtil.update(buyWhat, money, id)
        Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show()
        ObserManager.notifyObserver()
        buy_what.setText("")
        buy_money.setText("")
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isInteger(money: String): Boolean {
        val pattern = Pattern.compile("^[1-9]\\d*$")
        return pattern.matcher(money).matches()
    }

    private fun isDouble(money: String): Boolean {
        val pattern = Pattern.compile("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*\$")
        return pattern.matcher(money).matches()
    }
}