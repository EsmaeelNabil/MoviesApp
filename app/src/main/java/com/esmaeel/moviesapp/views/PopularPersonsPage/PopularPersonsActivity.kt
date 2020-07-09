package com.esmaeel.moviesapp.views.PopularPersonsPage


import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esmaeel.moviesapp.R
import com.esmaeel.moviesapp.Utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularPersonsActivity : AppCompatActivity() {

    private val viewModel: PopularsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showLoader()

        viewModel.personsData.observe(this, Observer { contract ->
            when (contract.status) {
                Status.LOADING -> showLoader()
            }
        })
    }

    fun showLoader() {
//        loader?.show()
    }

    fun hideLoader() {
//        loader?.hide()
    }
}