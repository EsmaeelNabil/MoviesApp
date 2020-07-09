package com.esmaeel.moviesapp.data.models


data class ErrorModel(
    val errors: List<String?>? = listOf(),
    val status_code: Int? = 0,
    val status_message: String? = "",
    val success: Boolean? = false
)