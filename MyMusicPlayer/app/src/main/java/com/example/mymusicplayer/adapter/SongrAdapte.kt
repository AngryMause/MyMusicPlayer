package com.example.mymusicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.entetis.Song
import kotlinx.android.synthetic.main.list_music_item.view.*
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private var glide: RequestManager
) :RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(viewV: View) : RecyclerView.ViewHolder(viewV)

    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }


        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var song: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        println(song.size.toString() + " Adapter size")
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_music_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holderSong: SongViewHolder, position: Int) {
        val songs = song[position]
        holderSong.itemView.apply {
            title_tx_list_rv.text = songs.title
            subtitle_item_rv.text = songs.subTitle
            glide.load(songs.imageUrl).into(image_song_list_im)
            setOnItemClickListener {
                onClickListener?.let { clic ->
                    clic(songs)
                }
            }
        }


    }


    private var onClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onClickListener = listener
    }


    override fun getItemCount() = song.size


}


