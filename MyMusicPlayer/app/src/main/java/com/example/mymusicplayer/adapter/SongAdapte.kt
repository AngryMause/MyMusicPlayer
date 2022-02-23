package com.example.mymusicplayer.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import kotlinx.android.synthetic.main.list_music_item.view.*
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private var glide: RequestManager
) : BaseAdapter(R.layout.list_music_item) {


    override val differ = AsyncListDiffer(this, diffCallback)


    override fun onBindViewHolder(holderSong: SongViewHolder, position: Int) {
        val songs = song[position]
        holderSong.itemView.apply {
            title_tx_list_rv.text = songs.title
            subtitle_item_rv.text = songs.subtitle
            glide.load(songs.imageUrl).into(image_song_list)
            setOnClickListener {onClickListener.let {
                    click -> click?.let { it1 -> it1(songs) } }
            }

        }
    }


}


