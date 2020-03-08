package com.baidu.yuepingxu.tallyapp.util

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.baidu.yuepingxu.tallyapp.MyApplication

/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
class SqliteHelper: SQLiteOpenHelper(MyApplication.application, "jojo_tally.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table jojo_tally(_id integer primary key autoincrement,buy_what text,money text, " +
                    "year integer, month integer, day integer, hour integer, minute integer)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}