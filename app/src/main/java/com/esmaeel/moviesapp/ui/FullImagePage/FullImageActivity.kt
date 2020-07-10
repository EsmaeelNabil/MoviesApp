package com.esmaeel.moviesapp.ui.FullImagePage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.LoadRequest
import com.esmaeel.moviesapp.R
import com.esmaeel.moviesapp.Utils.*
import com.esmaeel.moviesapp.databinding.ActivityFullImageBinding
import com.esmaeel.moviesapp.di.PROFILE_IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FullImageActivity : AppCompatActivity() {

    @Inject
    @PROFILE_IMAGE_BASE_URL
    lateinit var profileBaseUrl: String
    lateinit var binder: ActivityFullImageBinding
    private var canSave: Boolean = false;
    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityFullImageBinding.inflate(layoutInflater)
        setContentView(binder.root)
        loadUi()
    }


    /**
     * get the data from the previous page
     * then download the Image
     * convert it to Bitmap
     * be Ready for saving
     */
    private fun loadUi() {
        intent.getStringExtra(Constants.IMAGE_PATH)?.let { imagePath ->
            Coil.imageLoader(context = applicationContext).execute(
                LoadRequest.Builder(applicationContext)
                    .data(imagePath.buildProfilePath(profileBaseUrl))
                    .target(
                        onSuccess = { result ->
                            binder.photoView.setImageDrawable(result)
                            imageBitmap =
                                result.toBitmap(result.intrinsicWidth, result.intrinsicHeight)
                            canSave = true
                        },
                        onError = { error ->
                            canSave = false
                        })
                    .build()
            )
        }

        binder.saveImage.setOnClickListener {
            saveIfI(canSave)
        }
    }


    /**
     * Request the read and write Permissions.
     */
    private fun checkForPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }


    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            for (entry in map.entries) {
                /*if at least one is not granted then ask again!*/
                canSave = entry.value != false
            }
        }

    private fun saveIfI(canSave: Boolean) {
        if (canSave)
            CoroutinesManager.onIOThread {
                // Util for saving a bitmap to gallery
                ImageSaver.saveToGallery(
                    applicationContext,
                    imageBitmap,
                    Constants.ALBUM_NAME,
                    { /*Success*/
                        showSnackMessage(
                            getString(R.string.image_saved_successfully),
                            binder.root
                        )
                    },
                    { error ->/*Failed*/
                        showSnackMessage(error, binder.root)
                    })
            }
        /* show the user a message that tells him that he needs the permissions first to save*/
        else showSnackMessageWithAction(
            getString(R.string.save_permissions_info),
            binder.root,
            getString(R.string.accept)
        ) {
            /* Direct the user to app settings screen to accept the persmissions*/
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts("package", this.getPackageName(), null)
            intent.data = uri
            startActivity(intent)
        }
    }


    /**
     * check for permission again , in case the user accepted them in settings.
     */
    override fun onStart() {
        super.onStart()
        checkForPermissions()
    }


    companion object {
        fun startActivity(
            context: Context,
            imagePath: String
        ) {
            val intent = Intent(context, FullImageActivity::class.java).also {
                it.putExtra(Constants.IMAGE_PATH, imagePath)
            }
            context.startActivity(intent)
        }
    }

}