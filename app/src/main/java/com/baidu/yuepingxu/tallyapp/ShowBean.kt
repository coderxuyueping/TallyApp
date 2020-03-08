package com.baidu.yuepingxu.tallyapp

import java.io.Serializable


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */

data class ShowBean(var id: Int, var time: String, var buyWhat: String, var moeny: String) : Serializable