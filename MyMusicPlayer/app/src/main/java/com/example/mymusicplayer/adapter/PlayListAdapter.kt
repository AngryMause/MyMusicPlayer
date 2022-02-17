package com.example.mymusicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.entetis.PlayListModel
import kotlinx.android.synthetic.main.rv_playlist_stayle.view.*

class PlayListAdapter(private val playListStayle: ArrayList<PlayListModel>) :
    RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imageOne: ImageView = v.image_play_list_one
        val imageTwo: ImageView = v.image_play_list_two
        val imageThree: ImageView = v.image_play_list_three
        val imageFour: ImageView = v.image_play_list_four
        val namePlaylist: TextView = v.playlist_name


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_playlist_stayle, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curentItem = playListStayle[position]
        holder.imageOne.setBackgroundResource(curentItem.imageOne)
        holder.imageTwo.setBackgroundResource(curentItem.imageTwo)
        holder.imageThree.setBackgroundResource(curentItem.imageThree)
        holder.imageFour.setBackgroundResource(curentItem.imageFour)
        holder.namePlaylist.text = playListStayle[position].namePlaylist
    }

    override fun getItemCount() = playListStayle.size


}


