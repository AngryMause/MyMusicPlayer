package com.example.mymusicplayer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapter.SongAdapter
import com.example.mymusicplayer.model.SongModel
import com.example.mymusicplayer.other.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song_list.*
import javax.inject.Inject

@AndroidEntryPoint
class SongListFragment(
) : BaseFragment(R.layout.fragment_song_list) {

   private lateinit var list: ArrayList<SongModel>
    @Inject
    lateinit var songAdapter: SongAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAdapter()
        subscribeToObserver()
        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
        tv_shuffle.setOnClickListener {
            mainViewModel.shuffle()
        }
        list = ArrayList()
    }

    private fun setupRecyclerAdapter() = all_song_rv.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObserver() {
        mainViewModel.mediaItem.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    allSongsProgressBar.isVisible = false
                    result.data?.let { song ->
                        songAdapter.song = song
                        list.addAll(song)
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> allSongsProgressBar.isVisible = true

            }
        }

    }

    fun filter(name: String) {
        val filterList: ArrayList<SongModel> = ArrayList()

        for (songModel: SongModel in list) {
            if (songModel.title?.toLowerCase()!!.contains(name.toLowerCase())
                || songModel.subtitle?.toLowerCase()!!.contains(name.toLowerCase())) {
                filterList.add(songModel)
            }
        }
//        if (filterList.isEmpty()) {
//            Toast.makeText(requireContext(), " No Data found...", Toast.LENGTH_SHORT).show()
//
//        }else{
//            songAdapter.filteredList(filterList)
//        }
    }


}






