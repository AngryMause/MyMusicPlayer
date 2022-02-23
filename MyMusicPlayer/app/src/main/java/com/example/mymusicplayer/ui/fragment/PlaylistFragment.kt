package com.example.mymusicplayer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapter.PlayListAdapter
import com.example.mymusicplayer.other.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist.*
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : BaseFragment(R.layout.fragment_playlist) {


    @Inject
    lateinit var playListAdapter: PlayListAdapter
    private lateinit var layoutManager1: RecyclerView.LayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        subscribeToObservers()

    }


    private fun setUpRecycler() = rv_playlist.apply {
        adapter = playListAdapter
//        layoutManager = LinearLayoutManager(requireContext())
        layoutManager1 = GridLayoutManager(activity, SPAN_COUNT)
        with(rv_playlist) {
            layoutManager = this@PlaylistFragment.layoutManager1
            scrollToPosition(0)
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let { songs ->
                        playListAdapter.song = songs
                    }
                }
                Status.ERROR -> Unit
            }
        }
    }

    companion object {
        private var SPAN_COUNT = 2

    }


}