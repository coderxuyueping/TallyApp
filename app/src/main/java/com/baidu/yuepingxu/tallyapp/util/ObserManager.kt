package com.baidu.yuepingxu.tallyapp.util


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
object ObserManager {
    val list: MutableList<Observer> = mutableListOf()

    fun addObserver(observer: Observer) {
        list.add(observer)
    }

    fun notifyObserver() {
        list.forEach {
            it.onChange()
        }
    }

    fun remove(observer: Observer) {
        list.remove(observer)
    }


    interface Observer {
        fun onChange()
    }
}