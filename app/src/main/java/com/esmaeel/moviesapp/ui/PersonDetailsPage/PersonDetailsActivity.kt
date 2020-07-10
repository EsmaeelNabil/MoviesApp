package com.esmaeel.moviesapp.ui.PersonDetailsPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.api.load
import com.esmaeel.moviesapp.R
import com.esmaeel.moviesapp.Utils.*
import com.esmaeel.moviesapp.data.models.PersonsImagesResponse
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import com.esmaeel.moviesapp.databinding.ActivityPersonDetailsBinding
import com.esmaeel.moviesapp.di.IMAGES_ADAPTER
import com.esmaeel.moviesapp.di.PROFILE_IMAGE_BASE_URL
import com.esmaeel.moviesapp.ui.FullImagePage.FullImageActivity
import com.esmaeel.moviesapp.ui.PersonDetailsPage.Adapters.PersonsImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsActivity : AppCompatActivity() {

    @Inject
    @IMAGES_ADAPTER
    lateinit var imagesAdapter: PersonsImagesAdapter
    private val viewModel: PersonDetailsViewModel by viewModels()
    lateinit var binder: ActivityPersonDetailsBinding

    @Inject
    @PROFILE_IMAGE_BASE_URL
    lateinit var ProfileBaseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binder.root)


        initViews()
        initData()


    }

    private fun initViews() {
        binder.backButton.setOnClickListener {
            finish()
        }

        binder.imagesRecycler.apply {
            setHasFixedSize(false)
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = imagesAdapter
        }

        imagesAdapter.clickEvent.observe(this, Observer {
            openImageInFullScreen(it.data.file_path)
        })

    }

    private fun openImageInFullScreen(filePath: String?) {
        filePath?.let {
            FullImageActivity.startActivity(this, filePath)
        }
    }


    private fun initData() {
        viewModel.personsImages.observe(this, Observer { contract ->
            when (contract.status) {
                Status.SUCCESS -> {
                    hideLoader()
                    bindRemoteDataToUi(contract.data)
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

        intent.extras?.let { bundle ->
            val person =
                bundle.getSerializable(Constants.PERSON_DATA) as PopularPersonsResponse.Result
            person?.let { personn ->
                bindPersonInfo(personn)
                personn.id?.let { personId -> getPersonsImages(personId) }
            }
        }

    }

    private fun bindRemoteDataToUi(data: PersonsImagesResponse?) {
        data?.let {
            imagesAdapter.submitList(it.profiles)
        }
    }


    private fun showLoader() {
        binder.loaderLayout.root.Visible()
    }

    private fun hideLoader() {
        binder.loaderLayout.root.Gone()
    }

    private fun getPersonsImages(id: Int) {
        if (isNetworkAvailable())
            viewModel.getPersonImages(id)
        else showSnackMessage(getString(R.string.network_error), binder.root)
    }

    private fun bindPersonInfo(person: PopularPersonsResponse.Result) {
        binder.personImage.load(person.profile_path.buildProfilePath(ProfileBaseUrl))
        binder.name.text = person.name
        binder.department.text = person.known_for_department
        binder.knownFor.text = MyUtils.getKnownFor(person.known_for)
        binder.imagesTitle.text = "${person.name} ${getString(R.string.images)}"
    }

    companion object {
        fun startActivity(
            context: Context,
            person: PopularPersonsResponse.Result
        ) {
            val intent = Intent(context, PersonDetailsActivity::class.java)
            val bundle = Bundle().also {
                it.putSerializable(Constants.PERSON_DATA, person)
            }

            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}