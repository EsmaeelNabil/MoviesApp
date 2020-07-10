package com.esmaeel.moviesapp.ui.PersonDetailsPage

import com.esmaeel.moviesapp.Utils.Contract
import com.esmaeel.moviesapp.Utils.MyUtils
import com.esmaeel.moviesapp.data.remote.MoviesNetworkService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonDetailsRepository @Inject constructor(
    private val mService: MoviesNetworkService
) {

    fun getPersonsImages(person_id: Int?) = flow {
        emit(Contract.onLoading(data = null))
        try {
            val response = mService.getPersonImages(personId = person_id)

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