package com.baidu.yuepingxu.tallyapp.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.yuepingxu.tallyapp.Bean
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.ShowBean
import com.baidu.yuepingxu.tallyapp.util.LogUtil
import com.baidu.yuepingxu.tallyapp.util.ObserManager
import com.baidu.yuepingxu.tallyapp.util.SqliteUtil
import kotlinx.android.synthetic.main.fg_look.*
import java.math.BigDecimal


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class LookFragment : Fragment() {

    private var list: MutableList<ShowBean> = mutableListOf()
    private var allList: MutableList<Bean> = mutableListOf()
    private var adapter: MyAdapter? = null
    private var allIncome: Float = 0f
    private var allOutcome: Float = 0f
    private val observer: ObserManager.Observer = object : ObserManager.Observer {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onChange() {
            list.clear()
            initData()
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fg_look, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        val dialog = DatePickerDialog(context)
        sear_by_date.setOnClickListener {
            if (!dialog.isShowing)
                dialog.show()
        }
        dialog.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                LogUtil.log("year:$year, month:$month, dayOfMonth:$dayOfMonth")
                val intent = Intent(context, LookActivity::class.java)
                intent.putExtra(
                    "bean",
                    ShowBean(0, "" + year + "年" + (month + 1) + "月" + dayOfMonth + "日", "", "")
                )
                context?.startActivity(intent)

            }

        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = MyAdapter(list)
        recyclerView.adapter = adapter
        adapter?.listener = object : MyAdapter.Listener {
            override fun onClick(showBean: ShowBean) {
                val intent = Intent(context, LookActivity::class.java)
                intent.putExtra("bean", showBean)
                context?.startActivity(intent)
            }
        }


        adapter?.longClickListener = object : MyAdapter.LongClickListener {
            override fun onLongClick(showBean: ShowBean) {
                if (context == null) {
                    return
                }
                val dialog = AlertDialog.Builder(context!!)
                dialog.setTitle("删除")
                dialog.setMessage("确定要删除该条记录嘛?")
                dialog.setPositiveButton("确定", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val year = showBean.time.split("年")[0]
                        val month = showBean.time.split("年")[1].split("月")[0]
                        LogUtil.log("year:$year,month:$month")
                        SqliteUtil.deleteByDate(year, month)
                        Toast.makeText(context!!, "删除成功", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        ObserManager.remove(observer)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initData() {
        allOutcome = 0f
        allIncome = 0f
        val timeList: MutableMap<String, Float> = mutableMapOf<String, Float>()
        SqliteUtil.queryAll().forEach {
            val time = "" + it.year + "年" + it.month + "月份"
            var allMoney: Float = 0f
            if (!TextUtils.isEmpty(it.money)) {
                if (it.money.startsWith("-")) {
                    allMoney = -(it.money.split("-")[1]).toFloat()
                    allOutcome += allMoney
                } else {
                    allMoney = it.money.toFloat()
                    allIncome += allMoney
                }
            }
            if (timeList.containsKey(time)) {
                val money: Float = timeList.get(time)!!
                allMoney = BigDecimal(allMoney.toString()).add(BigDecimal(money.toString())).toFloat()
            }
            timeList.put(time, allMoney)
            allList.add(it)
        }

        timeList.forEach { t, u ->
            if (!TextUtils.isEmpty(u.toString())) {
                val showBean = ShowBean(0, t, "账单明细", u.toString())
                list.add(showBean)
            }
        }


        val temp = BigDecimal(allOutcome.toString()).add(BigDecimal(allIncome.toString()))
        tip.text = "总收入: $allIncome    总支出: $allOutcome    结余: $temp"
    }
}