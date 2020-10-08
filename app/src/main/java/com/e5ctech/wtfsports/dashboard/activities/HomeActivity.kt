package com.e5ctech.wtfsports.dashboard.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.fragments.*
import com.e5ctech.wtfsports.utils.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class HomeActivity : BaseActivity(){

    lateinit var rvHomeNavigation:RelativeLayout
    lateinit var tablayoutBottom:TabLayout

    lateinit var ivNavMenu:ImageView
    lateinit var containerNavigation:RelativeLayout
    lateinit var tvName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvHomeNavigation = findViewById(R.id.rvHomeNavigation)
        tablayoutBottom = findViewById(R.id.tablayoutbottom)
        ivNavMenu = findViewById(R.id.ivNavMenu)
        containerNavigation = findViewById(R.id.containerNavigation)
        tvName = findViewById(R.id.tvName)

        inittablayoutManager()
        loadFragment(HomeFragment())
        ivNavMenu.setOnClickListener {
            val unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.menu)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            loadNavigationFragment(NavigationFragment())
            if(containerNavigation.isVisible){
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.black))
                ivNavMenu.setImageDrawable(wrappedDrawable)
                containerNavigation.visibility = View.GONE
            }else{
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.colorAccent))
                ivNavMenu.setImageDrawable(wrappedDrawable)
                containerNavigation.visibility = View.VISIBLE
            }
        }
    }

    fun inittablayoutManager(){
        tablayoutBottom.addTab(tablayoutBottom.newTab().setIcon(R.drawable.home));
        tablayoutBottom.addTab(tablayoutBottom.newTab().setIcon(R.drawable.userfeed_black));
        tablayoutBottom.addTab(tablayoutBottom.newTab().setIcon(R.drawable.profile));
        tablayoutBottom.addTab(tablayoutBottom.newTab().setIcon(R.drawable.calendar));
        tablayoutBottom.setTabGravity(TabLayout.GRAVITY_FILL);

        for (i in 1..4) {
            val tabIconColor = ContextCompat.getColor(this@HomeActivity, R.color.colorAccent)
            tablayoutBottom.getTabAt(0)!!.icon!!.setColorFilter(
                tabIconColor,
                PorterDuff.Mode.SRC_IN
            )
        }

        tablayoutBottom.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                var fragment = Fragment();
                when (tab.position) {
                    0 -> {
                        fragment = HomeFragment()
                        tvName.text = "WTFMedia"
                    }
                    1 -> {
                        fragment = FeedsFragment()
                        tvName.text = "Personal Feeds"
                    }
                    2 -> {
                        fragment = ProfileFragment()
                        tvName.text = getUsersLocally().fullname
                    }
                    3 -> {
                        fragment = MatchCalendarFragment()
                        tvName.text = "Matches Calendar"
                    }
                }
                val tabIconColor = ContextCompat.getColor(this@HomeActivity, R.color.colorAccent)
                tablayoutBottom.getTabAt(tab.position)!!.icon!!.setColorFilter(
                    tabIconColor,
                    PorterDuff.Mode.SRC_IN
                )
                if(containerNavigation.isVisible){
                    containerNavigation.visibility = View.GONE
                    val unwrappedDrawable = AppCompatResources.getDrawable(this@HomeActivity, R.drawable.menu)
                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                    DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.black))
                    ivNavMenu.setImageDrawable(wrappedDrawable)
                }
                loadFragment(fragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(this@HomeActivity, R.color.black)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    private fun loadNavigationFragment(fragment: Fragment?): Boolean {

        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerNavigation, fragment)
                .commit()
            return true
        }
        return false
    }

    private fun loadFragment(fragment: Fragment?): Boolean {

        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }
}