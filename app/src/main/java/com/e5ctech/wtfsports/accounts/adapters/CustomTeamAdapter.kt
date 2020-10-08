package com.e5ctech.wtfsports.accounts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.BuildConfig
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.models.TeamLeauge
import com.squareup.picasso.Picasso

class CustomTeamAdapter(var sportsList:MutableList<TeamLeauge>,
                        val activity: AppCompatActivity,
                        val onitemClickistener: onItemClickistener
) : RecyclerView.Adapter<CustomTeamAdapter.CustomHolder>() {

    override fun getItemCount(): Int {
        return sportsList.size
    }

    interface onItemClickistener {
        fun itemClick(position: Int, teamlistLeage:List<TeamLeauge>)
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val teamSelect1 = sportsList[holder.adapterPosition]

        holder.bindProducts(teamSelect1,activity,onitemClickistener);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_team_adapter, parent, false)
        return CustomHolder(v)
    }

    class CustomHolder(v: View) : RecyclerView.ViewHolder(v) {

        val ivTeamLogo = itemView.findViewById(R.id.ivTeamLogo) as ImageView
        val tvTeamName = itemView.findViewById(R.id.tvTeamName) as TextView
        val cbUserPics = itemView.findViewById(R.id.cbSelectTeam) as CheckBox

        fun bindProducts(
          sportsLeage: TeamLeauge,
          activity: AppCompatActivity,
          onitemClickistener: onItemClickistener
        ) {
            val sportsListLeageList = mutableListOf<TeamLeauge>()

            if(sportsLeage.team_profile_image.isNotEmpty()){

                Picasso.get().load(BuildConfig.APP_HOST+sportsLeage.team_profile_image)
                    .error(R.drawable.no_image)
                    .into(ivTeamLogo);
            }else{
                Picasso.get().load(R.drawable.wtf_logo)
                    .placeholder(R.drawable.wtf_logo)
                    .into(ivTeamLogo);
            }

            tvTeamName.text = sportsLeage.team_name
            itemView.setOnClickListener {
            }

            if(sportsLeage.isSelected){
                cbUserPics.setButtonDrawable(R.drawable.cheked_box)
            }else{
                cbUserPics.setButtonDrawable(R.drawable.unchecked)
            }

            cbUserPics.setOnCheckedChangeListener { buttonView, isChecked ->
                sportsLeage.isSelected = isChecked
                if(sportsLeage.isSelected){
                    sportsListLeageList.add(sportsLeage)
                    cbUserPics.setButtonDrawable(R.drawable.cheked_box)
                }else{
                    sportsListLeageList.remove(sportsLeage)
                    cbUserPics.setButtonDrawable(R.drawable.unchecked)
                }

                onitemClickistener.itemClick(adapterPosition,sportsListLeageList)
            }
        }
    }

}