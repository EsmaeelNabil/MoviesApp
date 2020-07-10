package com.esmaeel.moviesapp.data.models

data class PersonsImagesResponse(
    val id: Int? = 0,
    val profiles: ArrayList<Profile?>? = arrayListOf()
) {
    data class Profile(
        val aspect_ratio: Double? = 0.0,
        val file_path: String? = "",
        val height: Int? = 0,
        val vote_average: Double? = 0.0,
        val vote_count: Int? = 0,
        val width: Int? = 0
    )
}