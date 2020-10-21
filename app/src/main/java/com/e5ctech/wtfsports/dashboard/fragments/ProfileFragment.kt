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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import bibou.biboubeauty.com.utils.ImageUtil
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.BuildConfig
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.activities.RegisterActivity
import com.e5ctech.wtfsports.accounts.adapters.FeedsAdapter
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.Feeds
import com.e5ctech.wtfsports.dashboard.model.FeedsParams
import com.e5ctech.wtfsports.dashboard.model.FeedsResponse
import com.e5ctech.wtfsports.utils.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feeds_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : BaseFragment() ,View.OnClickListener,FeedsAdapter.onItemMenuSelectedListener{


    lateinit var ivUserProfileimage:ImageView
    lateinit var ivCoverImage:ImageView
    lateinit var tvName:TextView
    lateinit var bnEditProfile:Button
    private var imageCaptureFile: Uri? = null
    val PICK_CAMERA_IMAGE_GALLERY = 235
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1003
    val IMAGE_DIRECTORY_NAME ="WTFMedia"
    var isCoverPhoto = false
    lateinit var rlProfileImage:RelativeLayout
    lateinit var rlCoverImage:RelativeLayout
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var rvFeeds: RecyclerView

    override fun setUp() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rooView = inflater.inflate(R.layout.fragment_profile, container, false)
        ivUserProfileimage = rooView.findViewById(R.id.ivUserProfileimage)
        ivCoverImage = rooView.findViewById(R.id.ivCoverImage)
        bnEditProfile = rooView.findViewById(R.id.bnEditProfile)
        tvName = rooView.findViewById(R.id.tvName)

        rlCoverImage = rooView.findViewById(R.id.rlCoverImage)
        rlProfileImage = rooView.findViewById(R.id.rlProfileImage)

        val etWriteFeed = rooView.findViewById<EditText>(R.id.etWriteFeed)
        val trmcq = rooView.findViewById<TableRow>(R.id.trmcq)
        val trPhoto = rooView.findViewById<TableRow>(R.id.trPhoto)
        val trPolls = rooView.findViewById<TableRow>(R.id.trPolls)
        rvFeeds = rooView.findViewById(R.id.rvFeeds)

        trmcq.setOnClickListener(this)
        trPolls.setOnClickListener(this)
        trPhoto.setOnClickListener(this)
        etWriteFeed.setOnClickListener(this)

        rlProfileImage.setOnClickListener(this)
        rlCoverImage.setOnClickListener(this)
        bnEditProfile.setOnClickListener(this)

        tvName.text = getBaseActivity()!!.getUsersLocally().fullname

        return rooView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        expandCloseBottomSheetBehaviour()
        getUser()
        getFeedsResponse()
    }

    fun getUser() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.getUserProfile(
                getBaseActivity()!!.getUsersLocally().id!!
            )

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    if(users.profile_image!=null && users.profile_image!!.isNotEmpty()){

                        Picasso.get().load(BuildConfig.APP_HOST + users.profile_image)
                            .error(R.drawable.userimage)
                            .into(ivUserProfileimage);
                    }else{
                        Picasso.get().load(R.drawable.userimage)
                            .placeholder(R.drawable.userimage)
                            .into(ivUserProfileimage);
                    }

                    if(users.cover_image!=null && users.cover_image!!.isNotEmpty()){

                        Picasso.get().load(BuildConfig.APP_HOST + users.cover_image)
                            .error(R.drawable.userimage)
                            .into(ivCoverImage);
                    }else{
                        Picasso.get().load(R.drawable.userimage)
                            .placeholder(R.drawable.userimage)
                            .into(ivCoverImage);
                    }
                    dismissProgressDialog()
                } else {
                    dismissProgressDialog()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
            }
        })
    }

    fun uploadProfileImage(photobase64value: String) {

        showProgressDialog()
        val users = Users()
        users.id = getBaseActivity()!!.getUsersLocally().id
        users.profile_image = photobase64value

        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.updateProfileImage(
                users.id!!,
                users
            )

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 201 || response.code() == 200 || response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    dismissProgressDialog()
                    showToast("Profile image updated successfully")
                } else {
                    dismissProgressDialog()
                    showToast("Error Occurred")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("error occured")
            }
        })
    }

    fun uploadCoverImage(photobase64value: String) {

        showProgressDialog()
        val users = Users()
        users.id = getBaseActivity()!!.getUsersLocally().id
        users.cover_image = photobase64value
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.updateCoverImage(
                getBaseActivity()!!.getUsersLocally().id!!,
                users
            )

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 201 || response.code() == 200 || response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();

                   /* if(users.profile_image!!.isNotEmpty()){

                        Picasso.get().load(BuildConfig.APP_HOST + users.profile_image)
                            .error(R.drawable.userimage)
                            .into(ivCoverImage);
                    }else{
                        Picasso.get().load(R.drawable.userimage)
                            .placeholder(R.drawable.userimage)
                            .into(ivCoverImage);
                    }*/

                    dismissProgressDialog()
                    showToast("Profile cover image updated successfully")
                } else {
                    dismissProgressDialog()
                    showToast("Error Occurred")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("error occured")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.rlProfileImage -> {
                isCoverPhoto = false
                selectImage()
            }

            R.id.rlCoverImage -> {
                isCoverPhoto = true
                selectImage()
            }

            R.id.bnEditProfile -> {
                val intent = Intent(requireActivity(), RegisterActivity::class.java)
                intent.putExtra("isForEdit",true)
                startActivity(intent)
            }
            R.id.trPolls->{
                gotoCreatePostActivity()
            }
            R.id.trmcq->{
                gotoCreatePostActivity()
            }
            R.id.trPhoto->{
                gotoCreatePostActivity()
            }
            R.id.etWriteFeed->{
                gotoCreatePostActivity()
            }
        }
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
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val readpermission =
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val camerapermission =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
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

        val imageUtil = ImageUtil(requireActivity())
        when (requestCode) {

            PICK_CAMERA_IMAGE_GALLERY -> {

                try {
                    var photoCopress: String? = null
                    var photoBase64Value: String? = null
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
                        val cursor = requireActivity().contentResolver.query(
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
                    if(isCoverPhoto){
                        ivCoverImage.setImageBitmap(photo)
                        uploadCoverImage(photoBase64Value!!)
                    }else{
                        ivUserProfileimage.setImageBitmap(photo)
                        uploadProfileImage(photoBase64Value!!)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun expandCloseBottomSheetBehaviour(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN
    }

    fun getFeedsResponse() {
        val feedsResponse = FeedsParams()
        feedsResponse.userid = getBaseActivity()!!.decodeString(getBaseActivity()!!.getUsersLocally().id!!)
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.getUserFeedsResponse(feedsResponse)

        call.enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(
                call: Call<FeedsResponse>,
                response: Response<FeedsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val feedsResponseFinal = response.body()!!;
                    val feedsAdapter = FeedsAdapter(feedsResponseFinal.Responce!!, getBaseActivity()!!,this@ProfileFragment)
                    rvFeeds.adapter = feedsAdapter
                }
            }

            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
                showToast(t.toString())
            }
        })
    }

    fun gotoCreatePostActivity(){
        val intent = Intent(requireActivity(),CreatePostFeedsActivity::class.java)
        startActivity(intent)
    }

    override fun onItemMenuClick(feeds: Feeds, pos: Int) {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCommentClick(feeds: Feeds) {

    }

    override fun onLikeClick(feeds: Feeds, pos: Int) {

    }

    override fun onShareClick(feeds: Feeds) {

    }


}