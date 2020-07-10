package com.esmaeel.moviesapp.ui.PersonDetailsPage.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.esmaeel.moviesapp.Utils.ViewEventsContract
import com.esmaeel.moviesapp.Utils.buildProfilePath
import com.esmaeel.moviesapp.Utils.combineClickListeners
import com.esmaeel.moviesapp.data.models.PersonsImagesResponse
import com.esmaeel.moviesapp.databinding.PersonImagesRecyclerItemBinding
import com.esmaeel.moviesapp.di.PROFILE_IMAGE_BASE_URL
import javax.inject.Inject

class PersonsImagesAdapter @Inject constructor(
    @PROFILE_IMAGE_BASE_URL val profileImageBaseUrl: String
) :
    ListAdapter<PersonsImagesResponse.Profile, PersonsImagesAdapter.PersonHolder>(
        PopularPersonsDiffUtil
    ) {

    val clickEvent: MutableLiveData<ViewEventsContract<PersonsImagesResponse.Profile>> =
        MutableLiveData()

    private object PopularPersonsDiffUtil :
        DiffUtil.ItemCallback<PersonsImagesResponse.Profile>() {
        override fun areItemsTheSame(
            oldItem: PersonsImagesResponse.Profile,
            newItem: PersonsImagesResponse.Profile
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PersonsImagesResponse.Profile,
            newItem: PersonsImagesResponse.Profile
        ) = oldItem.file_path == newItem.file_path
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        return PersonHolder(
            PersonImagesRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bindViews(getItem(position))
    }


    inner class PersonHolder(val binder: PersonImagesRecyclerItemBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews(item: PersonsImagesResponse.Profile) {
            binder.image.load(item.file_path?.buildProfilePath(profileImageBaseUrl))

            combineClickListeners(binder.root, binder.image) {
                clickEvent.value =
                    ViewEventsContract(
                        data = item,
                        position = absoluteAdapterPosition
                    )
            }

        }
    }


}
