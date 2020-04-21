package com.htec.task.utils

import android.content.Context
import android.content.SharedPreferences
import org.joda.time.DateTime

object SharedUtil {

    const val PREF_CACHE_CONTROL = "cacheControlPref"
    const val CACHE_LOCAL_TIME_KEY = "cacheLocalTime"

    /**
     * Function return {true} if local cache exists or cached data time is longer than 5 minutes,
     * otherwise return {false} value.
     * @param context {@link Context} - activity context */
    fun isLocalCacheExpired(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_CACHE_CONTROL, Context.MODE_PRIVATE)

        if (sharedPref.contains(CACHE_LOCAL_TIME_KEY)) {
            var localCacheTime = sharedPref.getLong(CACHE_LOCAL_TIME_KEY, 0L)
            var timeNow = DateTime().millis
            var timeDifferenceInMillis = Math.abs( timeNow - localCacheTime)
            // 5(min) * 60(sec) * 1000 milliseconds
            return timeDifferenceInMillis >= 300000
        }
        return true
    }

    fun refreshCacheTime(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_CACHE_CONTROL, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.putLong(CACHE_LOCAL_TIME_KEY, DateTime().millis)
        editor.apply()
    }

    fun clearCacheTime(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_CACHE_CONTROL, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.remove(CACHE_LOCAL_TIME_KEY)
        editor.apply()
    }

    fun hasLocalCache(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_CACHE_CONTROL, Context.MODE_PRIVATE)
        return (sharedPref.contains(CACHE_LOCAL_TIME_KEY))
    }
}