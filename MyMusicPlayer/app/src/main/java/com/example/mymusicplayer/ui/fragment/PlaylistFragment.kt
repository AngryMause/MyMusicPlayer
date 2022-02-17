package com.example.mymusicplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapter.PlayListAdapter
import com.example.mymusicplayer.data.entetis.PlayListModel

class PlaylistFragment : BaseFragment(R.layout.fragment_song_list) {
    private lateinit var list: ArrayList<PlayListModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        recyclerView= view.findViewById(R.id.rv_playlist)
        recyclerView.setHasFixedSize(true)
        layoutManager=GridLayoutManager(activity, SPAN_COUNT)
        with(recyclerView) {
            layoutManager = this@PlaylistFragment.layoutManager
            scrollToPosition(0)
        }
        recyclerView.adapter=PlayListAdapter(list)
        return view
    }

//    private fun initData(){
//        list=ArrayList()
//        for (i in 1..3){
//            list.add(PlayListModel(R.drawable.ic_mask_group_one,R.drawable.ic_mask_group_two,R.drawable.ic_mask_group_three,R.drawable.ic_mask_group_four, "My PlaylistName $i"))
//        }
//    }



    companion object {
        private var SPAN_COUNT = 2
        private var DATASET_COUNT=15
    }


}