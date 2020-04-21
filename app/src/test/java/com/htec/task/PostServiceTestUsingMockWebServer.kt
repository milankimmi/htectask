package com.htec.task

import com.htec.task.utils.TestApiCallInterface
import com.htec.task.utils.TestParseUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import junit.framework.TestCase
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val jsonResponseFileName = "posts_response.json"
const val END_POINT = "https://jsonplaceholder.typicode.com"

class PostServiceTestUsingMockWebServer {
    /**
     * MockWebServer - this is a library from OkHttp that allows you to run a local HTTP server in our test
     *
     * MockWebServer has a test rule you can use for your network tests. It is a scriptable web
     * server. You will supply it responses and it will return them on request.
     * To add the rule in out test class:
     * */
    @get:Rule
    val mockWebServer = MockWebServer()

    private val retrofit by lazy {
        // [1] Set the `baseUrl` on the builder using the mockWebServer. This is required when using Retrofit. Because you’re not hitting the network, “/” is valid.
        // [2] Add a call adapter. Using a RxJava call adapter allows you to return RxJava streams in our `PostServiceTestUsingMockWebServer`, helping you handle the asynchronous nature of the network calls.
        // [3] Add a converter factory. This is so you can use Gson to automatically convert the JSON to a nice Kotlin object and build it.
        Retrofit.Builder()
            .baseUrl(mockWebServer.url(END_POINT))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val postService by lazy {
        retrofit.create(TestApiCallInterface::class.java)
    }

    @Test
    fun testPostApiCall() {
        // [1] Use the mockWebServer that you created before to enqueue a response.
        // [2] You enqueue a response by building and passing in a MockResponse object.
        // [3] Use the testJson that you loaded as the body of the response.
        // [4]  Set the response code to 200 — success!
        mockWebServer.enqueue(
            MockResponse().setBody(TestParseUtils.getJson(jsonResponseFileName))
                .setResponseCode(200)
        )

        val testObserver = postService.getPosts().test()
        val localPosts = TestParseUtils.getPostTestObject(jsonResponseFileName)

        //Check is the first userId the same as we have in local .json file.
        TestCase.assertEquals(testObserver.values()[0][0].userId, localPosts[0].userId)

        //http://hamcrest.org/JavaHamcrest/tutorial
        assertThat(testObserver.values()[0][0].title,equalTo(localPosts[0].title))
    }

    @Test
    fun testPostApiPath() {
        mockWebServer.enqueue(
            MockResponse().setBody(TestParseUtils.getJson(jsonResponseFileName))
                .setResponseCode(200)
        )

        val testObserver = postService.getPosts().test()
        testObserver.assertNoErrors()
        TestCase.assertEquals(END_POINT, mockWebServer.takeRequest().path)
    }
}