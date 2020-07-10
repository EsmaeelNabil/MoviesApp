package com.esmaeel.moviesapp.Utils

import com.esmaeel.moviesapp.data.models.ErrorModel
import com.esmaeel.moviesapp.data.models.KnownFor
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MyUtilsTest {

    lateinit var originalRequest: Request
    lateinit var response: Response

    @Before
    fun setUp() {
        // dummy original request without the api key
        originalRequest = Request.Builder()
            .url("https://www.dummy.com/")
            .build();

    }

    @Test
    fun injectApiKey_given_original_request_and_returning_new_request_with_api_injected() {
        // data
        val result = MyUtils.injectApiKey(originalRequest, "API_KEY_VALUE")

        // expect
        val expectedApiKeyQuery = "API_KEY_VALUE"

        // assert
        Assert.assertEquals(expectedApiKeyQuery, result.queryParameter("api_key"))
    }

    @Test
    fun getErrorFromBody_From_404_status_code_with_status_message_string_presented() {
        // data ErrorModel
        val errorModel = ErrorModel(
            status_code = 34,
            status_message = "The resource you requested could not be found.",
            success = true
        )
        val messageJson =
            Gson().toJson(errorModel).toResponseBody("application/json".toMediaTypeOrNull())

        response = Response.Builder().request(originalRequest)
            .protocol(Protocol.HTTP_1_1)
            .body(messageJson)
            .code(404)
            .message("Message")
            .sentRequestAtMillis(-1L)
            .receivedResponseAtMillis(System.currentTimeMillis())
            .build()

        // expect
        val errorMessage = "The resource you requested could not be found."

        // result
        val result = MyUtils.getErrorFromBody(errorBody = response.body)


        // assertion
        Assert.assertEquals(errorMessage, result)

    }

    @Test
    fun getKnownFor_with_list_of_known_for_return_expected_result() {
        // data
        val knownForList: ArrayList<KnownFor?> = arrayListOf(
            KnownFor(original_title = "title_one"),
            KnownFor(original_title = "title_two"),
            KnownFor(original_title = "title_three"),
            KnownFor(original_title = "title_four")
        )

        // expected
        val expected = "Known For : title_one, title_two, title_three, title_four"

        // result
        val result = MyUtils.getKnownFor(knownFor = knownForList)

        //assert
        Assert.assertEquals(expected, result)

    }

    @Test
    fun getKnownFor_with_empty_list_returns_empty_string() {
        // data
        val knownForList: ArrayList<KnownFor?> = arrayListOf()

        // expected
        val expected = Constants.EMPTY_STRING

        // result
        val result = MyUtils.getKnownFor(knownFor = knownForList)

        //assert
        Assert.assertEquals(expected, result)

    }


}