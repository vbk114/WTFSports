package com.e5ctech.wtfsports.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.utils.base.BaseFragment


class NavigationFragment : BaseFragment() {
    lateinit var llogout:LinearLayoutCompat
    override fun setUp() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview  = inflater.inflate(R.layout.fragment_navigation, container, false)
        llogout = rootview.findViewById(R.id.llogout)

        llogout.setOnClickListener {
            getBaseActivity()!!.logout()
        }

        return rootview
    }
}