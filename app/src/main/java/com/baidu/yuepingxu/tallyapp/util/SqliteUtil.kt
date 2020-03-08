package com.baidu.yuepingxu.tallyapp.util

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import com.baidu.yuepingxu.tallyapp.Bean
import kotlin.math.log


/**
 * @author xuyueping
 * @date 2020-03-07
 * @describe
 */
object SqliteUtil {

    fun insert(bean: Bean): Boolean {
        val mySqliteHelper = SqliteHelper()
        val contentValues = ContentValues()
        contentValues.put("buy_what", bean.buyWhat)
        contentValues.put("money", bean.money)
        contentValues.put("year", bean.year)
        contentValues.put("month", bean.month)
        contentValues.put("day", bean.day)
        contentValues.put("hour", bean.hour)
        contentValues.put("minute", bean.minute)
        val result = mySqliteHelper.writableDatabase.insert("jojo_tally", null, contentValues)
        mySqliteHelper.close()
        mySqliteHelper.writableDatabase.close()
        return result != -1L
    }

    fun queryAll(): MutableList<Bean> {
        val list: MutableList<Bean> = mutableListOf()
        val mySqliteHelper = SqliteHelper()
        val sql = "select * from jojo_tally"
        val cursor = mySqliteHelper.readableDatabase.rawQuery(sql, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val _id = cursor.getInt(cursor.getColumnIndex("_id"))
                val buy_what = cursor.getString(cursor.getColumnIndex("buy_what"))
                val money = cursor.getString(cursor.getColumnIndex("money"))
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val month = cursor.getInt(cursor.getColumnIndex("month"))
                val day = cursor.getInt(cursor.getColumnIndex("day"))
                val hour = cursor.getInt(cursor.getColumnIndex("hour"))
                val minute = cursor.getInt(cursor.getColumnIndex("minute"))
                val bean = Bean(_id, buy_what, money, year, month, day, hour, minute)
                list.add(bean)
                LogUtil.log(
                    "id:" + _id + ",buy:" + buy_what + ",money:" + money + ",year:" + year + ",month:" + month + ",day:" + day + ",hour:" + hour + ",minute:" + minute
                )
            }
            cursor.close()
        }
        mySqliteHelper.writableDatabase.close()
        mySqliteHelper.close()
        return list
    }

    // 根据年份和月份查出
    fun queryByDate(mYear: Int, mMonth: Int, mDay: Int = 0): MutableList<Bean> {
        val list: MutableList<Bean> = mutableListOf()
        val mySqliteHelper = SqliteHelper()
        var sql = ""
        if (mDay == 0) {
            sql = "select * from jojo_tally where year=$mYear and month=$mMonth order by day asc"
        } else {
            sql =
                "select * from jojo_tally where year=$mYear and month=$mMonth and day=$mDay order by hour"
        }

        val cursor = mySqliteHelper.readableDatabase.rawQuery(sql, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val _id = cursor.getInt(cursor.getColumnIndex("_id"))
                val buy_what = cursor.getString(cursor.getColumnIndex("buy_what"))
                val money = cursor.getString(cursor.getColumnIndex("money"))
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val month = cursor.getInt(cursor.getColumnIndex("month"))
                val day = cursor.getInt(cursor.getColumnIndex("day"))
                val hour = cursor.getInt(cursor.getColumnIndex("hour"))
                val minute = cursor.getInt(cursor.getColumnIndex("minute"))
                val bean = Bean(_id, buy_what, money, year, month, day, hour, minute)
                list.add(bean)
                LogUtil.log(
                    "id:$_id,buy:$buy_what,money:$money,year:$year,month:$month,day:$day,hour:$hour,minute:$minute"
                )
            }
            cursor.close()
        }
        mySqliteHelper.writableDatabase.close()
        mySqliteHelper.close()
        return list
    }

    fun update(buyWhat: String, money: String, id: Int) {
        val mySqliteHelper = SqliteHelper()
        var sql = ""
        if (TextUtils.isEmpty(buyWhat)) {
            sql = "update jojo_tally set money='$money' where _id=$id"
        } else if (TextUtils.isEmpty(money)) {
            sql = "update jojo_tally set buy_what='$buyWhat' where _id=$id"
        } else {
            sql = "update jojo_tally set money='$money', buy_what='$buyWhat' where _id=$id"
        }

        mySqliteHelper.writableDatabase.execSQL(sql)
        mySqliteHelper.writableDatabase.close()
        mySqliteHelper.close()
    }

    fun deleteById(id: Int) {
        val mySqliteHelper = SqliteHelper()
        var sql = "delete from jojo_tally where _id=$id"
        mySqliteHelper.writableDatabase.execSQL(sql)
        mySqliteHelper.writableDatabase.close()
        mySqliteHelper.close()
    }

    fun deleteByDate(year: String, month: String) {
        val mySqliteHelper = SqliteHelper()
        var sql = "delete from jojo_tally where year=$year and month=$month"
        mySqliteHelper.writableDatabase.execSQL(sql)
        mySqliteHelper.writableDatabase.close()
        mySqliteHelper.close()
    }

}