package com.e5ctech.wtfsports.dashboard.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.adapters.FeedsAdapter
import com.e5ctech.wtfsports.dashboard.model.*
import com.e5ctech.wtfsports.modules.module_comments.CommentsFragment
import com.e5ctech.wtfsports.utils.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.feeds_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedsFragment : BaseFragment(),View.OnClickListener,FeedsAdapter.onItemMenuSelectedListener {

    override fun setUp() {

    }

    private var postLikePos: Int = -1
    private var postLikeFeed: Feeds? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var rvFeeds:RecyclerView
    var vbg: View? = null
    var selectedfeeds: Feeds? = null
    var feedsList:MutableList<Feeds>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.feeds_list_fragment, container, false)
        val etWriteFeed = view.findViewById<EditText>(R.id.etWriteFeed)
        val trmcq = view.findViewById<TableRow>(R.id.trmcq)
        val trPhoto = view.findViewById<TableRow>(R.id.trPhoto)
        val trPolls = view.findViewById<TableRow>(R.id.trPolls)
        rvFeeds = view.findViewById(R.id.rvFeeds)
        vbg = view.findViewById(R.id.v_bg)
        trmcq.setOnClickListener(this)
        trPolls.setOnClickListener(this)
        trPhoto.setOnClickListener(this)
        etWriteFeed.setOnClickListener(this)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        trEdit.setOnClickListener {
            if (selectedfeeds != null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                vbg!!.visibility = View.GONE
                val intent = Intent(requireActivity(), CreatePostFeedsActivity::class.java)
                var args = Bundle()
                args.putSerializable("feed", selectedfeeds)
                intent.putExtras(args)
                startActivity(intent)
            }
        }

        trDelete.setOnClickListener {
            if (selectedfeeds != null){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                vbg!!.visibility = View.GONE
                deletePostDialog()
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        vbg!!.visibility = View.GONE
        expandCloseBottomSheetBehaviour()

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        vbg!!.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        vbg!!.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        if (vbg!!.visibility == View.VISIBLE){
                            vbg!!.visibility = View.GONE
                        } else {
                            vbg!!.visibility = View.VISIBLE
                        }
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }

    private fun deletePostDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_delete_post)

        val btn_cancel = dialog.findViewById(R.id.btn_cancel) as TextView
        val btn_unsend = dialog.findViewById(R.id.btn_delete) as TextView

        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

        btn_unsend.setOnClickListener {
            dialog.dismiss()
            deletePost(selectedfeeds)
        }

        dialog.show()
    }

    private fun deletePost(selectedfeed: Feeds?) {
        showProgressDialog()
        var call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.deletePost(selectedfeed!!.id)

        call!!.enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(
                call: Call<FeedsResponse>,
                response: Response<FeedsResponse>
            ) {

                if (response.isSuccessful) {

                    val defaultResponse = response.body();
                    val users = defaultResponse!!.Responce
                    dismissProgressDialog()
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

    override fun onResume() {
        super.onResume()
        getFeedsResponse()
    }

    fun expandCloseBottomSheetBehaviour(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN
        vbg!!.visibility = View.GONE
    }

    fun getFeedsResponse() {
        showProgressDialog()
        val feedsResponse = FeedsParams()
        feedsResponse.senderid = getBaseActivity()!!.getUsersLocally().id!!
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.getFeedsResponse(feedsResponse)

        call.enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(
                call: Call<FeedsResponse>,
                response: Response<FeedsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    dismissProgressDialog()
                    val feedsResponseFinal = response.body()!!
                    feedsList = feedsResponseFinal.Responce!!
                    val feedsAdapter = FeedsAdapter(feedsList!!, getBaseActivity()!!,this@FeedsFragment)
                    rvFeeds.adapter = feedsAdapter
                } else {
                    dismissProgressDialog()
                }
            }

            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast(t.toString())
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
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

    fun gotoCreatePostActivity(){
        val intent = Intent(requireActivity(),CreatePostFeedsActivity::class.java)
        startActivity(intent)
    }

    override fun onItemMenuClick(feeds: Feeds) {
        selectedfeeds = feeds
        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            vbg!!.visibility = View.VISIBLE
        } else {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
            vbg!!.visibility = View.GONE
        }
    }

    override fun onCommentClick(feeds: Feeds) {
        var fragment = Fragment()
        var args = Bundle()
        args.putSerializable("post", feeds)
        fragment = CommentsFragment()
        fragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("fragment_more")
            .commit()
    }

    override fun onLikeClick(feeds: Feeds, pos: Int) {
        this.postLikeFeed = feeds
        this.postLikePos = pos
        postLike(feeds)
    }

    override fun onShareClick(feeds: Feeds) {
        val intent = Intent(requireActivity(), CreatePostFeedsActivity::class.java)
        var args = Bundle()
        args.putBoolean("is_share", true)
        args.putSerializable("feed", feeds)
        intent.putExtras(args)
        startActivity(intent)
    }

    private fun postLike(feeds: Feeds) {
        showProgressDialog()
        val feedsResponse = LikePostParams()
        var userid = getBaseActivity()!!.decodeString(getBaseActivity()!!.getUsersLocally().id!!)
        feedsResponse.likeuserid = getBaseActivity()!!.decodeString(getBaseActivity()!!.getUsersLocally().id!!)
        feedsResponse.likepostid = feeds.id.toString()
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.likePostResponse(feedsResponse)

        call.enqueue(object : Callback<LikePostResponse> {
            override fun onResponse(
                call: Call<LikePostResponse>,
                response: Response<LikePostResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val feedsResponseFinal = response.body()!!
                    dismissProgressDialog()
                    if (feedsResponseFinal.status){
                        if (feedsResponseFinal.Response.like){
                            feedsList!!.get(postLikePos).is_like = true
                        } else {
                            feedsList!!.get(postLikePos).is_like = false
                        }
                    }
                    /*val feedsAdapter = FeedsAdapter(feedsList!!, getBaseActivity()!!,this@FeedsFragment)
                    rvFeeds.adapter = feedsAdapter*/
                    rvFeeds.adapter!!.notifyDataSetChanged()
                } else {
                    dismissProgressDialog()
                }
            }

            override fun onFailure(call: Call<LikePostResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast(t.toString())
            }
        })
    }
}