package com.baidu.yuepingxu.tallyapp.util

import android.content.Context


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */


fun Context.SpSet(name: String, first: Boolean) {
    val sp = getSharedPreferences("JOJOSP", Context.MODE_PRIVATE)
    sp.edit().putBoolean(name, first).apply()
}

fun Context.Spget(name: String): Boolean {
    val sp = getSharedPreferences("JOJOSP", Context.MODE_PRIVATE)
    return sp.getBoolean(name, true)
}