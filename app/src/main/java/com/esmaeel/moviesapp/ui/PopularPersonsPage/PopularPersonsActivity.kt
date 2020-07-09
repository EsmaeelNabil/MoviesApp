package com.esmaeel.moviesapp.ui.PopularPersonsPage


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.esmaeel.moviesapp.R
import com.esmaeel.moviesapp.Utils.Constants
import com.esmaeel.moviesapp.Utils.EndlessRecyclerOnScrollListener
import com.esmaeel.moviesapp.Utils.Status
import com.esmaeel.moviesapp.Utils.showSnackMessage
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import com.esmaeel.moviesapp.databinding.ActivityPopularPersonsBinding
import com.esmaeel.moviesapp.ui.PopularPersonsPage.Adapter.PopularPersonsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularPersonsActivity : AppCompatActivity() {

    @Inject
    lateinit var popularPersonsAdapter: PopularPersonsAdapter
    private val viewModel: PopularsViewModel by viewModels()
    private lateinit var binder: ActivityPopularPersonsBinding
    private lateinit var paginationListener: EndlessRecyclerOnScrollListener
    private var personsList: MutableList<PopularPersonsResponse.Result?>? = null
    private var pageNumber = Constants.FirstPage
    private var hasMorePages: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityPopularPersonsBinding.inflate(layoutInflater)
        setContentView(binder.root)

        initData()
        initViews()
        requestPageNumber(pageNumber)
    }


    private fun requestPageNumber(pageNumber: Int) {
        viewModel.getPersonsData(pageNumber)
    }

    private fun initViews() {
        binder.swipe.apply {
            setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
            )
            setOnRefreshListener {
                this.isRefreshing = false
            }
        }

        paginationListener = object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int, lastVisibleItemPosition: Int) {
                pageNumber = currentPage
                requestPageNumber(pageNumber)
            }
        }

        binder.recycler.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = popularPersonsAdapter
            addOnScrollListener(paginationListener)
        }

        popularPersonsAdapter.clickEvent.observe(this, Observer {
            showSnackMessage(it.data.name!!, binder.root)
        })


    }


    private fun initData() {
        viewModel.personsData.observe(this, Observer { contract ->
            when (contract.status) {
                Status.SUCCESS -> {
                    hideLoader()
                    bindDataToUi(contract.data)
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.ERROR -> {
                    hideLoader()
                    showSnackMessage(contract.message ?: "", binder.root)
                }
            }
        })
    }

    private fun bindDataToUi(data: PopularPersonsResponse?) {
        data?.let {
            hasMorePages = data.hasMorePages()

            if (!personsList.isNullOrEmpty()) personsList!!.addAll(data.results!!) else personsList =
                data.results

            popularPersonsAdapter.submitList(personsList)
        }
    }


    fun showLoader() {
        binder.swipe.isRefreshing = true
    }

    fun hideLoader() {
        binder.swipe.isRefreshing = false
    }
}