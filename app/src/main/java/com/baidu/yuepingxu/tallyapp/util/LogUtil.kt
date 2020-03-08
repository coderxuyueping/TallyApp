package com.baidu.yuepingxu.tallyapp.util

import android.text.TextUtils
import android.util.Log
import com.baidu.yuepingxu.tallyapp.BuildConfig


/**
 * @author xuyueping
 * @date 2020-03-08
 * @describe
 */
object LogUtil {
    var openLog = BuildConfig.DEBUG
    fun log(content: String?){
        if(openLog && !TextUtils.isEmpty(content)){
            Log.d("xuyueping", content)
        }
    }
}