package com.esmaeel.moviesapp.ui.PopularPersonsPage

import com.esmaeel.moviesapp.Utils.Contract
import com.esmaeel.moviesapp.Utils.Status
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PopularsRepositoryTest {

    @Mock
    lateinit var repository: PopularsRepository

    /**
     * here we test if we started to send the first page number == zero
     */
    @Test
    fun getPersonsData_With_zero_as_an_input_for_page_number_and_return_no_with_error() {
        runBlocking {
            //Mock the data
            `when`(repository.getPersonsData(0))
                .thenReturn(
                    flow {
                        emit(
                            Contract(
                                data = null,
                                message = "page must be greater than 0",
                                status = Status.ERROR
                            )
                        )
                    }
                )

            var result: Status? = null
            var expectedresult: Status? = Status.ERROR

            // Call the function

            repository.getPersonsData(0).collect {
                result = it.status
            }

            // assertion
            Assert.assertEquals(expectedresult, result)
        }
    }

    /**
     * here we test if we started to send the first page number == one
     */
    @Test
    fun getPersonsData_With_one_as_an_input_for_page_number_and_return_data_with_success() {
        runBlocking {
            //Mock the data
            `when`(repository.getPersonsData(1))
                .thenReturn(
                    flow {
                        emit(
                            Contract(
                                data = PopularPersonsResponse(page = 1),
                                message = null,
                                status = Status.SUCCESS
                            )
                        )
                    }
                )

            var result: Contract<PopularPersonsResponse?>? = null
            val expectedresult = Contract(
                data = PopularPersonsResponse(page = 1),
                message = null,
                status = Status.SUCCESS
            )

            // Call the function

            repository.getPersonsData(1).collect {
                result = it
            }

            // assertion
            Assert.assertEquals(expectedresult.data?.page, result?.data?.page)

        }
    }

}