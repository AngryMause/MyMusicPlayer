package com.example.mymusicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.data.entetis.Song

abstract class BaseAdapter (
    private val layoutId: Int) :
    RecyclerView.Adapter<BaseAdapter.SongViewHolder>() {

    class SongViewHolder(viewV: View) : RecyclerView.ViewHolder(viewV)

    protected val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<Song>

    var song: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )

    }


    protected var onClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onClickListener = listener
    }


    override fun getItemCount() = song.size


}