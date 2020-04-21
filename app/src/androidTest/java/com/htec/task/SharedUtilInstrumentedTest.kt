package com.htec.task

import android.app.AlertDialog
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.htec.task.utils.SharedUtil
import com.htec.task.utils.TimeMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedUtilInstrumentedTest {

    lateinit var context: Context
    lateinit var timeMock: TimeMock

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        timeMock = TimeMock()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test
        assertEquals("com.htec.task", context.applicationContext.packageName)
    }

    fun test(){
        var activityMonitor = getInstrumentation().addMonitor(MainActivity::class.java.name, null, false)
        val mainActivity: MainActivity = activityMonitor.waitForActivity() as MainActivity
        getInstrumentation().waitForIdleSync()

        //fun getDialog() : AlertDialog{
        //    return alertDialog!!
        //}
        //val dialog : AlertDialog = mainActivity.getDialog()

        getInstrumentation().removeMonitor(activityMonitor)
    }
    @Test
    fun hasLocalCache() {
        //Clear cache when run this function
        SharedUtil.clearCacheTime(context)
        var hasLocallCache: Boolean = SharedUtil.hasLocalCache(context)
        assertEquals("Local cache not exist", false, hasLocallCache)

        SharedUtil.refreshCacheTime(context)
        hasLocallCache = SharedUtil.hasLocalCache(context)
        assertEquals("Local cache exist", true, hasLocallCache)
    }

    @Test
    fun isLocalCacheExpired() {
        //Clear cache when run this function
        SharedUtil.clearCacheTime(context)
        val cacheExpired: Boolean = SharedUtil.isLocalCacheExpired(context)

        assertEquals(
            "Local cache is expired, request a new content from the server.",
            true,
            cacheExpired
        )
    }

    @Test
    fun isLocalCacheNotExpired() {
        //Clear cache when run this function
        SharedUtil.clearCacheTime(context)
        SharedUtil.refreshCacheTime(context)
        val cacheExpired: Boolean = SharedUtil.isLocalCacheExpired(context)

        assertEquals(
            "Local cache is NOT expired, then return local cached data.",
            false,
            cacheExpired
        )
    }

    @Test
    fun timePassesCheckCacheExpirationAfter3Minutes() {
        SharedUtil.clearCacheTime(context)
        SharedUtil.refreshCacheTime(context)

        val sharedPref =
            context.getSharedPreferences(SharedUtil.PREF_CACHE_CONTROL, Context.MODE_PRIVATE)
        val newTime = timeMock.timePasses(3)
        val editor = sharedPref.edit()
        editor.putLong(SharedUtil.CACHE_LOCAL_TIME_KEY, newTime)
        editor.apply()

        val cacheExpired: Boolean = SharedUtil.isLocalCacheExpired(context)
        assertEquals(
            "Local cache still NOT expired, return local cached data.",
            false,
            cacheExpired
        )
    }

    @Test
    fun timePassesCheckCacheExpirationAfter5Minutes() {
        SharedUtil.clearCacheTime(context)
        SharedUtil.refreshCacheTime(context)

        val sharedPref = context.getSharedPreferences(SharedUtil.PREF_CACHE_CONTROL, Context.MODE_PRIVATE)
        val newTime = timeMock.timePasses(5)
        val editor = sharedPref.edit()
        editor.putLong(SharedUtil.CACHE_LOCAL_TIME_KEY, newTime)
        editor.apply()

        val cacheExpired: Boolean = SharedUtil.isLocalCacheExpired(context)
        // After 5 min and plus 1 second, local cache is expired
        assertEquals(
            "Local cache is expired, request a new content from the server.",
            true,
            cacheExpired
        )

    }
}