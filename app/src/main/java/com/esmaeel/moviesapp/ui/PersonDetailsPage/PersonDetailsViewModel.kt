package com.esmaeel.moviesapp.ui.PersonDetailsPage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esmaeel.moviesapp.Utils.Contract
import com.esmaeel.moviesapp.data.models.PersonsImagesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PersonDetailsViewModel @ViewModelInject constructor(
    private val repository: PersonDetailsRepository
) : ViewModel() {

    private var _personsImagesData: MutableLiveData<Contract<PersonsImagesResponse?>> = MutableLiveData()

    val personsImages: LiveData<Contract<PersonsImagesResponse?>>
        get() = _personsImagesData

    fun getPersonImages(person_id: Int) {
        viewModelScope.launch {
            repository.getPersonsImages(person_id = person_id)
                .flowOn(Dispatchers.IO)
                .collect { data ->
                    _personsImagesData.value = data
                }
        }
    }

}