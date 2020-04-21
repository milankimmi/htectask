package com.htec.task

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.htec.task.utils.ConnectivityUtil
import org.hamcrest.core.Is
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectivityUtilInstrumentedTest {

    lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test
        assertEquals("com.htec.task", context.applicationContext.packageName)
    }

    @Test
    fun checkIfDeviceHasInternetConnection() {
        val hasConnection = ConnectivityUtil.hasActiveInternetConnection(context)
        assertThat("If the device has internet connection then result have to be: true", hasConnection, Is.`is`(true))
    }

    @Test
    fun checkIfDeviceDoesNotHaveInternetConnection() {
        val hasConnection = ConnectivityUtil.hasActiveInternetConnection(context)
        assertThat("If the device has internet connection then result have to be: false", hasConnection, Is.`is`(false))
    }

}