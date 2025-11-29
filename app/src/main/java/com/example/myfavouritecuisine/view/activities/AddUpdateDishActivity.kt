package com.example.myfavouritecuisine.view.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.ActivityAddUpdateDishBinding
import com.example.myfavouritecuisine.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private var currentPhotoUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if(result.resultCode == RESULT_OK) {
            currentPhotoUri?.let {
                Glide
                    .with(this)
                    .load(it)
                    .circleCrop()
                    .into(mBinding.ivDishImage);
            mBinding.ivAddDishImage.setImageResource(R.drawable.ic_vector_edit)
            }
        } else {
            // user cancelled, delete the empty entry
            currentPhotoUri?.let {
                contentResolver.delete(it, null, null)
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { sourceUri ->
                copyImageToInternalStorage(sourceUri)
                Glide
                    .with(this)
                    .load(currentPhotoUri)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            dataSource?.let {
                                if (it == DataSource.REMOTE) {
                                    copyImageToInternalStorage(sourceUri)
                                }
                            }
                            return false
                        }

                    })
                    .into(mBinding.ivDishImage);
            mBinding.ivAddDishImage.setImageResource(R.drawable.ic_vector_edit)
            }
        }

    }

    private fun copyImageToInternalStorage(sourceUri: Uri) : Boolean {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "dish_image.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/MyFavouriteCuisine")
            }
            val destinationUri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            destinationUri?.let { destUri ->
                contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                    contentResolver.openOutputStream(destUri)?.use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                currentPhotoUri = destUri
            }
            return true
        } catch (ex: Exception) {
            Log.e("AddUpdateDishActivity", "copyImageToInternalStorage: ", ex)
            Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar()
        mBinding.ivAddDishImage.setOnClickListener(this)
    }


    private fun setUpActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if(p0!=null) {
            when(p0.id){
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                }
            }
        }
    }

    fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.tvCamera.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                // Android 13 and above
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                    .withListener(object: MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            report?.let {
                                if(report.areAllPermissionsGranted()) {
                                    storeImageAfterCameraLaunch()
                                } else {
                                    showRationalDialogForPermissions()
                                }
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest?>?,
                            token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }

                    })
                    .onSameThread()
                    .check()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                openCamera()
            } else {
                // Android 9 and below (API ≤ 28)
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(object: MultiplePermissionsListener{
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                            storeImageAfterCameraLaunch()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: List<PermissionRequest?>?,
                            p1: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    })
            }
            dialog.dismiss()

        }
        binding.tvGallery.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            report?.let {
                                if(report.areAllPermissionsGranted()) {
                                    val galleryIntent = Intent(Intent.ACTION_PICK)
                                    galleryIntent.type = "image/*"
                                    galleryLauncher.launch(galleryIntent)
                                } else {
                                    showRationalDialogForPermissions()
                                }
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: List<PermissionRequest?>?,
                            p1: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }

                    })
                    .onSameThread()
                    .check()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            TODO("Not yet implemented")
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                            TODO("Not yet implemented")
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                        ) {
                            TODO("Not yet implemented")
                        }


                    })
            }
            dialog.dismiss()
        }
    }


    private fun openCamera() {
        // Android 10-12L (API 29-32)
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()) {
                            storeImageAfterCameraLaunch()
                        } else {
                            showRationalDialogForPermissions()
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: List<PermissionRequest?>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            })
    }

    private fun storeImageAfterCameraLaunch() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "dish_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/MyFavouriteCuisine")
        }

        currentPhotoUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        currentPhotoUri?.let { uri ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(cameraIntent)
        }

    }
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions required for this feature." +
                " It can be enabled under the Applications Settings")
            .setPositiveButton("Go to settings")
            { _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch(ex: Exception) {
                    ex.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val CAMERA = 1
        val GALLERY = 2
    }
}


