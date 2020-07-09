package com.esmaeel.moviesapp.ui.PopularPersonsPage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esmaeel.moviesapp.Utils.Contract
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PopularsViewModel @ViewModelInject constructor(
    private val repository: PopularsRepository
) : ViewModel() {

    private var _personsData: MutableLiveData<Contract<PopularPersonsResponse?>> = MutableLiveData()

    var personsData: LiveData<Contract<PopularPersonsResponse?>> = MutableLiveData()
        get() = _personsData


    fun getPersonsData(pageNumber: Int) {
        viewModelScope.launch {
            repository.getPersonsData(pageNumber = pageNumber)
                .flowOn(Dispatchers.IO)
                .collect { data ->
                    _personsData.value = data
                }
        }
    }

}