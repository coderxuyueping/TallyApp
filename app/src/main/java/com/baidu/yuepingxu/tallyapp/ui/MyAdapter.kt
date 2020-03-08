package com.baidu.yuepingxu.tallyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.baidu.yuepingxu.tallyapp.R
import com.baidu.yuepingxu.tallyapp.ShowBean


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class MyAdapter(var list: List<ShowBean>) : Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null
    var longClickListener: LongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_empty,
                    parent,
                    false
                )
            )
        }
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) 0 else 1
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) 1 else list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyViewHolder) {
            return
        }
        val myViewHolder = holder as MyViewHolder
        myViewHolder.time?.text = list[position].time
        myViewHolder.buyWhat?.text = list[position].buyWhat
        myViewHolder.buyMoney?.text = list[position].moeny + "元"

        myViewHolder.itemView.setOnClickListener {
            listener?.onClick(list[position])
        }
        myViewHolder.itemView.setOnLongClickListener {
            longClickListener?.onLongClick(list[position])
            true
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time: TextView? = null
        var buyWhat: TextView? = null
        var buyMoney: TextView? = null

        init {
            time = view.findViewById(R.id.time)
            buyWhat = view.findViewById(R.id.buy_what)
            buyMoney = view.findViewById(R.id.buy_money)
        }

    }

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface Listener{
        fun onClick(showBean: ShowBean)
    }

    interface LongClickListener{
        fun onLongClick(showBean: ShowBean)
    }

}