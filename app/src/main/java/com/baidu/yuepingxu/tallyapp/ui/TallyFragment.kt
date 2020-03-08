package com.baidu.yuepingxu.tallyapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baidu.yuepingxu.tallyapp.Bean
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.util.ObserManager
import com.baidu.yuepingxu.tallyapp.util.SqliteUtil
import kotlinx.android.synthetic.main.fg_tally.*
import java.util.*
import java.util.regex.Pattern


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class TallyFragment : Fragment() {
    var isIncome = false

    val pattern = "^[0-9]+(\\.?[0-9]+)?\$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fg_tally, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save.setOnClickListener {
            val buyWhat = buy_what.text.toString()
            val money = buy_money.text.toString().trim()
            if (TextUtils.isEmpty(buyWhat) || TextUtils.isEmpty(money)) {
                Toast.makeText(it.context, "不能不写哦", Toast.LENGTH_SHORT).show()
            } else {
                if (isInteger(money) || isDouble(money)) {
                    save(buyWhat, money)
                } else {
                    Toast.makeText(it.context, "金额只能是整数或者小数哦", Toast.LENGTH_SHORT).show()
                }
            }
        }

        class_rg.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                isIncome = checkedId == R.id.income
            }

        })
    }

    private fun save(buyWhat: String, money: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        var myMoney = "0"

        if (!isIncome) {
            myMoney = "-$money"
        } else {
            myMoney = money
        }

        val bean = Bean(0, buyWhat, myMoney, year, month, day, hour, minute)

        if (SqliteUtil.insert(bean)) {
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
            buy_what.setText("")
            buy_money.setText("")
            ObserManager.notifyObserver()
        } else {
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
        }
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