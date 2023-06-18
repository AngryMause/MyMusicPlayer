package com.example.mymusicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.model.SongModel

abstract class BaseAdapter (
    private val layoutId: Int) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {


    class BaseViewHolder(viewV: View) : RecyclerView.ViewHolder(viewV)


    protected val diffCallback = object : DiffUtil.ItemCallback<SongModel>() {
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<SongModel>

    var song: List<SongModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )

    }
    protected var onClickListener: ((SongModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (SongModel) -> Unit) {
        onClickListener = listener
    }


    override fun getItemCount() = song.size


}