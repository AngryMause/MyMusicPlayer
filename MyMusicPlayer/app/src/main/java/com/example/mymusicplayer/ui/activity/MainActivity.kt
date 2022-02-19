package com.example.mymusicplayer.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.entetis.Song
import com.example.mymusicplayer.exoplayer.toSong
import com.example.mymusicplayer.other.Status.*
import com.example.mymusicplayer.ui.fragment.SongListFragment
import com.example.mymusicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.botom_curren_song_navigation.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val listOfSongs = SongListFragment()

//    private lateinit var songList: List<Song>

    private val mainViewModel: MainViewModel by viewModels()
    private var curPlayingSong: Song? = null

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showListFragment(View(this))
        subscribeToObservers()
        btn_play_pause_bottom_menu.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }
//        println("SongList MainActivity ${songList.size}")


    }

    fun showListFragment(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()
    }


    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(this) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if (songs.isNotEmpty()) {
                                setDataToBottomVIew(songs)
                            }
                        }
                    }
                    ERROR -> Unit
                    LOADING -> Unit
                }
            }
        }

        mainViewModel.curPlayingSong.observe(this) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
        }
    }

    private fun setDataToBottomVIew(songs: List<Song>) {
        glide.load(
            (curPlayingSong ?: songs[0]).imageUrl
        )
            .into(image_bottom_menu)
        tv_song_name_bottom_menu.text =
            (curPlayingSong ?: songs[0].title).toString()
        tv_artist_name_bottom_menu.text =
            (curPlayingSong ?: songs[0].subTitle).toString()

    }

}





