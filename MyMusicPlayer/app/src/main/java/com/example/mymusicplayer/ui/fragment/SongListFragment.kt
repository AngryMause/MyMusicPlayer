package com.example.mymusicplayer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapter.SongAdapter
import com.example.mymusicplayer.other.Status
import com.example.mymusicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song_list.*
import javax.inject.Inject

@AndroidEntryPoint
class SongListFragment:Fragment(R.layout.fragment_song_list) {

    lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var songAdapter: SongAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        subscribeToObserver()
        setupRecyclerAdapter()
        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
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
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> allSongsProgressBar.isVisible = true

            }
        }
    }
}






