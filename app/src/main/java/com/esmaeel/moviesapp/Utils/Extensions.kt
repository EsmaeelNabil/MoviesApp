package com.esmaeel.moviesapp.Utils


fun String.isJson(): Boolean {
    return this.startsWith("{") || this.startsWith("[")
}