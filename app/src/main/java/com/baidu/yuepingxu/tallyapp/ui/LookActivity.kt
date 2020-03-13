package com.baidu.yuepingxu.tallyapp.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.yuepingxu.tallyapp.Bean
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.ShowBean
import com.baidu.yuepingxu.tallyapp.util.LogUtil
import com.baidu.yuepingxu.tallyapp.util.ObserManager
import com.baidu.yuepingxu.tallyapp.util.SqliteUtil
import kotlinx.android.synthetic.main.activity_look.*
import kotlinx.android.synthetic.main.activity_look.recycler_view
import kotlinx.android.synthetic.main.activity_look.tip
import kotlinx.android.synthetic.main.fg_look.*
import java.math.BigDecimal


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class LookActivity : AppCompatActivity() {
    private var showBean: ShowBean? = null
    private var dataList: MutableList<Bean>? = null
    private var list: MutableList<ShowBean> = mutableListOf()
    private var adapter: MyAdapter? = null
    private var allIncome: BigDecimal = 0f.toBigDecimal()
    private var allOutcome: BigDecimal = 0f.toBigDecimal()

    private val observer: ObserManager.Observer = object : ObserManager.Observer {
        override fun onChange() {
            list.clear()
            initData()
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_look)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        showBean = intent.extras?.get("bean") as ShowBean?
        LogUtil.log(showBean?.toString())
        actionBar?.title = showBean?.time ?: "JOJO"
        initData()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view.layoutManager = layoutManager
        adapter = MyAdapter(list)
        recycler_view.adapter = adapter
        adapter?.listener = object : MyAdapter.Listener {
            override fun onClick(showBean: ShowBean) {
                val isIncome = !showBean.moeny.startsWith("-")
                val intent = Intent(this@LookActivity, UpdateActivity::class.java)
                intent.putExtra("id", showBean.id)
                intent.putExtra("isIncome", isIncome)
                startActivity(intent)
            }
        }

        adapter?.longClickListener = object : MyAdapter.LongClickListener {
            override fun onLongClick(showBean: ShowBean) {
                val dialog = AlertDialog.Builder(this@LookActivity)
                dialog.setTitle("删除")
                dialog.setMessage("确定要删除该条记录嘛?")
                dialog.setPositiveButton("确定", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        SqliteUtil.deleteById(showBean.id)
                        Toast.makeText(this@LookActivity, "删除成功", Toast.LENGTH_SHORT).show()
                        ObserManager.notifyObserver()
                    }

                })
                dialog.setNegativeButton("取消", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }

                })
                dialog.show()
            }

        }

        ObserManager.addObserver(observer)
    }

    private fun initData() {
        allOutcome = 0f.toBigDecimal()
        allIncome = 0f.toBigDecimal()
        val time = showBean?.time
        val year = time!!.split("年")[0]
        val month = time.split("年")[1].split("月")[0]
        val day = time.split("年")[1].split("月")[1].split("日")[0]
        LogUtil.log("年:$year, 月:$month, 日:$day")
        dataList = SqliteUtil.queryByDate(year.toInt(), month.toInt(), day.toIntOrNull() ?: 0)
        dataList?.forEach {
            if (!TextUtils.isEmpty(it.money) && !TextUtils.isEmpty(it.buyWhat)) {
                val showBean = ShowBean(
                    it.id, "" + it.month + "月" + it.day + "日" + it.hour + "时" + it.minute + "分",
                    it.buyWhat, it.money
                )
                list.add(showBean)

                if (it.money.startsWith("-")) {
                    allOutcome = allOutcome.add(-(it.money.split("-")[1]).toFloat().toBigDecimal())
                } else {
                    allIncome = allIncome.add(it.money.toFloat().toBigDecimal())
                }
            }
        }

        val temp = allIncome.add(allOutcome)
        tip.text = "总收入: $allIncome    总支出: $allOutcome    结余: $temp"
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserManager.remove(observer)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}