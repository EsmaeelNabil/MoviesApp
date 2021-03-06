package com.esmaeel.moviesapp.ui.PopularPersonsPage.Adapter

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
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import com.esmaeel.moviesapp.data.models.Results
import com.esmaeel.moviesapp.databinding.PersonRecyclerItemBinding
import com.esmaeel.moviesapp.di.PROFILE_IMAGE_BASE_URL
import javax.inject.Inject

class PopularPersonsAdapter @Inject constructor(
    @PROFILE_IMAGE_BASE_URL val profileImageBaseUrl: String
) :
    ListAdapter<Results, PopularPersonsAdapter.PersonHolder>(
        PopularPersonsDiffUtil
    ) {

    val clickEvent: MutableLiveData<ViewEventsContract<Results>> =
        MutableLiveData()

    private object PopularPersonsDiffUtil :
        DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(
            oldItem: Results,
            newItem: Results
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Results,
            newItem: Results
        ) = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        return PersonHolder(
            PersonRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bindViews(getItem(position))
    }


    inner class PersonHolder(val binder: PersonRecyclerItemBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews(item: Results) {


            binder.personImage.load(item.profile_path?.buildProfilePath(profileImageBaseUrl))
            binder.nameTextview.text = item.name


            combineClickListeners(binder.root, binder.personImage, binder.nameTextview) {
                clickEvent.value =
                    ViewEventsContract(
                        data = item,
                        position = absoluteAdapterPosition
                    )
            }


        }
    }


}
