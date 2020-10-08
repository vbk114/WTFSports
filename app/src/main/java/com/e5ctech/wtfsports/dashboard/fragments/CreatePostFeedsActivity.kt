package com.e5ctech.wtfsports.dashboard.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import bibou.biboubeauty.com.utils.ImageUtil
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.model.Feeds
import com.e5ctech.wtfsports.dashboard.model.FeedsResponse
import com.e5ctech.wtfsports.utils.base.BaseActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreatePostFeedsActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    private var imageCaptureFile: Uri? = null
    val PICK_CAMERA_IMAGE_GALLERY = 235
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1003
    val IMAGE_DIRECTORY_NAME ="WTFMedia"

    lateinit var trPhoto:TableRow
    lateinit var trPolls:TableRow
    lateinit var trmcq:TableRow
    lateinit var etPostFeed:EditText
    lateinit var photoBase64Value:String
    lateinit var ivPostImage:ImageView
    lateinit var tvName:TextView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_feeds)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.create_post)
        setBackEnabled(true)

        trmcq = findViewById(R.id.trmcq)
        trPolls = findViewById(R.id.trPolls)
        trPhoto = findViewById(R.id.trPhoto)
        etPostFeed = findViewById(R.id.etPostFeed)
        ivPostImage = findViewById(R.id.ivPostImage)
        tvName = findViewById(R.id.tvName)

        tvName.text = getUsersLocally().fullname

        trPhoto.setOnClickListener{
            selectImage()
        }
    }

    fun savePostFeeds() {
        showProgressDialog()

        val userId = decodeString(getUsersLocally().id!!)
        val feeds = Feeds()
        feeds.postimage = photoBase64Value
        feeds.posttext = etPostFeed.text.toString()

        val call = BibouApiClient
            .instance(this@CreatePostFeedsActivity)
            .usersApi.savePost(userId,feeds)

        call.enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(
                call: Call<FeedsResponse>,
                response: Response<FeedsResponse>
            ) {

                if (response.isSuccessful) {

                    val defaultResponse = response.body();
                    val users = defaultResponse!!.Responce
                    dismissProgressDialog()
                    finish()
                } else {
                    dismissProgressDialog()
                    showToast("error occured")
                   // finish()
                }
            }

            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
                dismissProgressDialog()
                //finish()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImage() {
        if (checkAndRequestPermissions()) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageCaptureFile = getOutputMediaFileUri()
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureFile)

            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val chooser = Intent.createChooser(galleryIntent, "Choose your option")

            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            startActivityForResult(chooser, PICK_CAMERA_IMAGE_GALLERY)
        }
    }

    // converting to Uri format from file, for further process
    fun getOutputMediaFileUri(): Uri {
        return Uri.fromFile(getOutputMediaFile())
    }

    private fun getOutputMediaFile(): File? {

        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY_NAME
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    "Profile", "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory"
                )
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        var mediaFile: File? = null

        mediaFile = File(
            mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + ".jpg"
        )

        return mediaFile
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkAndRequestPermissions(): Boolean {
        val writepermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val readpermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val camerapermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val listPermissionsNeeded = ArrayList<String>()

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(
                listPermissionsNeeded.toArray(arrayOfNulls<String>(listPermissionsNeeded.size)),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {

            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices) {
                        if (permissions[i] == Manifest.permission.CAMERA) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                Log.e("msg", "Camera granted")
                            }
                        } else if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                Log.e("msg", "Write External storage granted")
                            }
                        } else if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                Log.e("msg", "Read External Storage granted")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageUtil = ImageUtil(this)
        when (requestCode) {

            PICK_CAMERA_IMAGE_GALLERY -> {

                try {
                    var photoCopress: String? = null
                    var photo: Bitmap? = null

                    if (data == null) {

                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true
                        BitmapFactory.decodeFile(
                            File(imageCaptureFile!!.path).absolutePath,
                            options
                        )
                        val imageHeight = options.outHeight
                        val imageWidth = options.outWidth

                        photoCopress =
                            imageUtil.compressImage(
                                imageCaptureFile.toString(),
                                imageWidth.toFloat(),
                                imageHeight.toFloat()
                            )
                        val options1 = BitmapFactory.Options()
                        val bitmap = BitmapFactory.decodeFile(photoCopress, options1);
                        photo =
                            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true);
                        photoBase64Value = imageUtil.encodeTobase64(photo)

                    } else if (data.data != null) {

                        // When an Image is picked
                        val selectedImage = data.data
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        // Get the cursor
                        val cursor = this.contentResolver.query(
                            selectedImage!!,
                            filePathColumn,
                            null,
                            null,
                            null
                        )
                        // Move to first row
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val image = cursor.getString(columnIndex)
                        cursor.close()
                        val sourceImage = Uri.parse("file:////$image")

                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true
                        BitmapFactory.decodeFile(File(sourceImage!!.path).absolutePath, options)
                        val imageHeight = options.outHeight
                        val imageWidth = options.outWidth

                        photoCopress =
                            imageUtil.compressImage(
                                selectedImage.toString(),
                                imageWidth.toFloat(),
                                imageHeight.toFloat()
                            )
                        val options1 = BitmapFactory.Options()
                        val bitmap = BitmapFactory.decodeFile(photoCopress, options1);
                        photo =
                            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true);
                        photoBase64Value = imageUtil.encodeTobase64(photo)

                    }

                    ivPostImage.setImageBitmap(photo)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.create_post->{
                if(etPostFeed.text.toString().isNotEmpty()){
                    savePostFeeds()
                }else{
                    showToast("Write your post atleast one char")
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}