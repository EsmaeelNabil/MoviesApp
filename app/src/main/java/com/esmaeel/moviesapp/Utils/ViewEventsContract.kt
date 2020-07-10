package com.esmaeel.moviesapp.Utils

import android.view.View

/*
*  this handles the Clicks + Holding information
*  about the clicked view, and it's position.
* */
data class ViewEventsContract<out T /* is the out class type whatever the type*/>(
    val data: T,
    val position: Int = 0,
    val isAction: Boolean = false,
    val viewID: Int = -1,
    val view: View? = null
) {
    companion object {
        fun <T> onClick(
            data: T,
            position: Int = 0,
            isAction: Boolean = false,
            viewID: Int = -1,
            view: View? = null
        ): ViewEventsContract<T> =
            ViewEventsContract(
                data = data, position = position, isAction = isAction, viewID = viewID, view = view
            )
    }
}