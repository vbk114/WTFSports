package com.e5ctech.wtfsports.accounts.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.TeamSelectionResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.adapters.CustomTeamAdapter
import com.e5ctech.wtfsports.accounts.models.Sports
import com.e5ctech.wtfsports.accounts.models.SportsLeauge
import com.e5ctech.wtfsports.accounts.models.TeamLeauge
import com.e5ctech.wtfsports.dashboard.activities.HomeActivity
import com.e5ctech.wtfsports.utils.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamSelectionActivity : BaseActivity(), CustomTeamAdapter.onItemClickistener {

    private lateinit var tabTeams: TabLayout
    private var tabItems = mutableListOf<Sports>()

    private lateinit var llTeam1: LinearLayout
    private lateinit var llTeam2: LinearLayout
    private lateinit var llTeam3: LinearLayout
    private lateinit var llTeam4: LinearLayout
    private lateinit var llTeam5: LinearLayout
    private lateinit var llTeam6: LinearLayout
    private lateinit var llTeam7: LinearLayout
    private lateinit var llTeam8: LinearLayout

    private lateinit var rvTeam1: RecyclerView;
    private lateinit var rvTeam2: RecyclerView;
    private lateinit var rvTeam3: RecyclerView;
    private lateinit var rvTeam4: RecyclerView;
    private lateinit var rvTeam5: RecyclerView;
    private lateinit var rvTeam6: RecyclerView;
    private lateinit var rvTeam7: RecyclerView;
    private lateinit var rvTeam8: RecyclerView;

    private lateinit var tvHeader1: TextView
    private lateinit var tvHeader2: TextView
    private lateinit var tvHeader3: TextView
    private lateinit var tvHeader4: TextView
    private lateinit var tvHeader5: TextView
    private lateinit var tvHeader6: TextView
    private lateinit var tvHeader7: TextView
    private lateinit var tvHeader8: TextView

    private var teamListResponse = TeamSelectionResponse()

    private lateinit var toolbar: Toolbar;
    private lateinit var bnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_selection)

        initViews()
        title = "Teams You Love"
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getSportsTeamList()

    }

    fun initViews() {

        tabTeams = findViewById(R.id.tabs)

        llTeam1 = findViewById(R.id.llTeam1)
        llTeam2 = findViewById(R.id.llTeam2)
        llTeam3 = findViewById(R.id.llTeam3)
        llTeam4 = findViewById(R.id.llTeam4)
        llTeam5 = findViewById(R.id.llTeam5)
        llTeam6 = findViewById(R.id.llTeam6)
        llTeam7 = findViewById(R.id.llTeam7)
        llTeam8 = findViewById(R.id.llTeam8)

        rvTeam1 = findViewById(R.id.rvTeam1)
        rvTeam2 = findViewById(R.id.rvTeam2)
        rvTeam3 = findViewById(R.id.rvTeam3)
        rvTeam4 = findViewById(R.id.rvTeam4)
        rvTeam5 = findViewById(R.id.rvTeam5)
        rvTeam6 = findViewById(R.id.rvTeam6)
        rvTeam7 = findViewById(R.id.rvTeam7)
        rvTeam8 = findViewById(R.id.rvTeam8)

        tvHeader1 = findViewById(R.id.tvHeader1)
        tvHeader2 = findViewById(R.id.tvHeader2)
        tvHeader3 = findViewById(R.id.tvHeader3)
        tvHeader4 = findViewById(R.id.tvHeader4)
        tvHeader5 = findViewById(R.id.tvHeader5)
        tvHeader6 = findViewById(R.id.tvHeader6)
        tvHeader7 = findViewById(R.id.tvHeader7)
        tvHeader8 = findViewById(R.id.tvHeader8)

        toolbar = findViewById(R.id.toolbar)
        bnNext = findViewById(R.id.bnNext)

        bnNext.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    fun initTabLayoutPager() {
        for (item in tabItems) {
            tabTeams.addTab(tabTeams.newTab().setText(item.sportsname))
        }
        tabTeams.setSelectedTabIndicator(R.color.colorPrimary)
        setIntialTab()
        tabTeams.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                val teamListLeage = getSportsteamList(tabItems[position].sportsname)
                setTeamList(teamListLeage)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun setIntialTab() {
        val tab = tabTeams.getTabAt(0)
        tab!!.select()
        val teamListLeage = getSportsteamList(tabItems[0].sportsname)
        setTeamList(teamListLeage)
    }

    fun getSportsTeamList() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@TeamSelectionActivity)
            .usersApi.getSportsChoiceList()

        call.enqueue(object : Callback<TeamSelectionResponse> {
            override fun onResponse(
                call: Call<TeamSelectionResponse>,
                response: Response<TeamSelectionResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    teamListResponse = response.body()!!;
                    tabItems = teamListResponse.Response.sports
                    if (tabItems.size > 0) {
                        initTabLayoutPager()
                    }
                    dismissProgressDialog()

                } else {
                    dismissProgressDialog()

                }
            }

            override fun onFailure(call: Call<TeamSelectionResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast(t.toString())
            }
        })
    }

    fun getSportsteamList(teamName: String): MutableList<SportsLeauge> {
        var teamListLeage = mutableListOf<SportsLeauge>()
        for (sposrts in teamListResponse.Response.sports) {
            if (sposrts.sportsname == teamName) {
                teamListLeage = sposrts.league_sport_id
            }
        }
        return teamListLeage;
    }

    fun setTeamList(teamListLeage: MutableList<SportsLeauge>) {

        for (item in 0 until teamListLeage.size) {
            val teamListLeageFinal = teamListLeage[item]

            when (item) {
                0 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader1.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam1, rvTeam1, tvHeader1, true)
                        setAdapter(rvTeam1, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam1, rvTeam1, tvHeader1, false)
                    }
                }
                1 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader2.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam2, rvTeam2, tvHeader2, true)
                        setAdapter(rvTeam2, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam2, rvTeam2, tvHeader2, false)
                    }
                }
                2 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader3.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam3, rvTeam3, tvHeader3, true)
                        setAdapter(rvTeam3, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam3, rvTeam3, tvHeader3, false)
                    }

                }
                3 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader4.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam4, rvTeam4, tvHeader4, true)
                        setAdapter(rvTeam4, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam4, rvTeam4, tvHeader4, false)
                    }
                }
                4 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader5.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam5, rvTeam5, tvHeader5, true)
                        setAdapter(rvTeam5, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam5, rvTeam5, tvHeader5, false)
                    }
                }
                5 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader6.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam6, rvTeam6, tvHeader6, true)
                        setAdapter(rvTeam6, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam6, rvTeam6, tvHeader6, false)
                    }
                }
                6 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader7.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam7, rvTeam7, tvHeader7, true)
                        setAdapter(rvTeam7, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam7, rvTeam7, tvHeader7, false)
                    }
                }
                7 -> {
                    if (teamListLeageFinal.team_leauge_id.isNotEmpty()) {
                        tvHeader8.text = teamListLeageFinal.leauge_name
                        setTeamListVisible(llTeam8, rvTeam8, tvHeader8, true)
                        setAdapter(rvTeam8, teamListLeageFinal.team_leauge_id)
                    } else {
                        setTeamListVisible(llTeam8, rvTeam8, tvHeader8, false)
                    }
                }
            }
        }
    }

    fun setAdapter(recyclerView: RecyclerView, teamListLeage: MutableList<TeamLeauge>) {
        val customTeamAdapter = CustomTeamAdapter(teamListLeage, this,this)
        recyclerView.adapter = customTeamAdapter
    }

    fun setTeamListVisible(view1: View, view2: View, view3: View, isShow: Boolean) {
        if (isShow) {
            view1.visibility = View.VISIBLE
            view2.visibility = View.VISIBLE
            view3.visibility = View.VISIBLE
        } else {
            view1.visibility = View.GONE
            view2.visibility = View.GONE
            view3.visibility = View.GONE
        }

    }

    override fun itemClick(position: Int, teamlistLeage: List<TeamLeauge>) {
       // showToast(teamlistLeage.size.toString())
    }

}