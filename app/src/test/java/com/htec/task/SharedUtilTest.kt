package com.htec.task

import android.content.Context
import android.content.SharedPreferences
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedUtilTest {

    companion object {
        val CACHE_LOCAL_TIME_KEY = "cacheLocalTime"
    }

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var mMockEditor: SharedPreferences.Editor

    @Mock
    lateinit var mMockSharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        context = mock(Context::class.java)

        Mockito.`when`(mMockSharedPreferences.getLong(eq(CACHE_LOCAL_TIME_KEY), anyLong())).thenReturn(200L)
        Mockito.`when`(mMockSharedPreferences.contains(eq(CACHE_LOCAL_TIME_KEY))).thenReturn(true)
        Mockito.`when`(mMockSharedPreferences.edit()).thenReturn(mMockEditor)
        Mockito.`when`(mMockEditor.commit()).thenReturn(true)
    }

    @Test
    fun test() {
        val containtKey = mMockSharedPreferences.contains(CACHE_LOCAL_TIME_KEY)
        assertThat("hasLocalCache", containtKey, Is.`is`(true))

        val cacheTimePass = mMockSharedPreferences.getLong(CACHE_LOCAL_TIME_KEY, 0L)
        assertThat("cacheTime", cacheTimePass, Is.`is`(200L))
    }
}
