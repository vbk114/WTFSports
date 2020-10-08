package com.e5ctech.wtfsports.dashboard.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TableRow
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.adapters.FeedsAdapter
import com.e5ctech.wtfsports.dashboard.model.Feeds
import com.e5ctech.wtfsports.dashboard.model.FeedsResponse
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

    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var rvFeeds:RecyclerView

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

        trmcq.setOnClickListener(this)
        trPolls.setOnClickListener(this)
        trPhoto.setOnClickListener(this)
        etWriteFeed.setOnClickListener(this)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        expandCloseBottomSheetBehaviour()
        getFeedsResponse()
    }

    fun expandCloseBottomSheetBehaviour(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN
    }

    fun getFeedsResponse() {
        showProgressDialog()
        val feedsResponse = FeedsResponse()
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
                    val feedsResponseFinal = response.body()!!;
                    val feedsAdapter = FeedsAdapter(feedsResponseFinal.Responce!!, getBaseActivity()!!,this@FeedsFragment)
                    rvFeeds.adapter = feedsAdapter
                    dismissProgressDialog()
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
        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        } else {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }


}