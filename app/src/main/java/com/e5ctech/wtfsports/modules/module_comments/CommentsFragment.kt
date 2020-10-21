package com.e5ctech.wtfsports.modules.module_comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.dashboard.activities.HomeActivity
import com.e5ctech.wtfsports.dashboard.model.CommentPostParams
import com.e5ctech.wtfsports.dashboard.model.Feeds
import com.e5ctech.wtfsports.databinding.FragmentCommentsBinding
import com.e5ctech.wtfsports.utils.Utils
import com.e5ctech.wtfsports.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsFragment : BaseFragment() {

    private var commentsAdapter: CommentsAdapter? = null
    private lateinit var binding: FragmentCommentsBinding
    var post: Feeds? = null
    override fun setUp() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as HomeActivity).hideTopBottom()
        post = requireArguments().getSerializable("post") as Feeds?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.commentsRecycler.layoutManager = LinearLayoutManager(activity)

        getPostComments()
        setComments()
    }

    private fun getPostComments() {

    }

    private fun setComments() {
        commentsAdapter = CommentsAdapter(requireActivity(), post!!.comments)
        binding.commentsRecycler.adapter = commentsAdapter
    }

    private fun setOnClickListener() {
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnSend.setOnClickListener {
            if (binding.etComment.text!!.trim().toString().equals("")){
                Toast.makeText(activity, "Comment cannot be empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.btnSend.isEnabled = false
            Utils.hideKeyboard(requireActivity())
            sendComment()
        }
    }

    private fun sendComment() {
        showProgressDialog()
        var commentParams = CommentPostParams()
        commentParams.comments = binding.etComment.text.toString()
        commentParams.comment_postid = post!!.id
        commentParams.comment_postuserid = getBaseActivity()!!.decodeString(getBaseActivity()!!.getUsersLocally().id!!).toInt()
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .usersApi.commentPostResponse(
                post!!.id,
                getBaseActivity()!!.decodeString(getBaseActivity()!!.getUsersLocally().id!!),
                commentParams
            )

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    binding.btnSend.isEnabled = true
                    binding.etComment.setText("")
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    dismissProgressDialog()
                    commentsAdapter!!.notifyDataSetChanged()
                } else {
                    binding.btnSend.isEnabled = true
                    dismissProgressDialog()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.btnSend.isEnabled = true
                dismissProgressDialog()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as HomeActivity).showTopBottom()
    }
}