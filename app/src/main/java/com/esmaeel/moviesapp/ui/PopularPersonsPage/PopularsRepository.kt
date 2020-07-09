package com.esmaeel.moviesapp.ui.PopularPersonsPage

import com.esmaeel.moviesapp.Utils.Contract
import com.esmaeel.moviesapp.Utils.MyUtils
import com.esmaeel.moviesapp.data.remote.MoviesNetworkService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PopularsRepository @Inject constructor(
    private val mService: MoviesNetworkService
) {

    fun getPersonsData(pageNumber: Int = 0) = flow {
        emit(Contract.onLoading(data = null))
        try {
            val response = mService.getPopularPersons(pageNumber = pageNumber)

            if (response.isSuccessful) {
                emit(Contract.onSuccess(data = response.body()))
            } else {
                response.errorBody()?.let { errorBody ->
                    emit(
                        Contract.onError(
                            data = null,
                            message = MyUtils.getErrorFromBody(errorBody)
                        )
                    )
                }
            }
        } catch (a: Exception) {
            a.localizedMessage?.let {
                emit(
                    Contract.onError(
                        data = null,
                        message = it
                    )
                )
            }
        }

    }

}