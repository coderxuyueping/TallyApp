package com.baidu.yuepingxu.tallyapp

import android.app.Application
import android.content.Context




/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class MyApplication : Application() {
    companion object {
        lateinit var application: Context
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}