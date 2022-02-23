package com.example.mymusicplayer.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.entetis.Song
import com.example.mymusicplayer.exoplayer.toSong
import com.example.mymusicplayer.other.Status.*
import com.example.mymusicplayer.ui.fragment.PlaylistFragment
import com.example.mymusicplayer.ui.fragment.SongListFragment
import com.example.mymusicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.botom_curren_song_navigation.*
import kotlinx.android.synthetic.main.botom_curren_song_navigation.view.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val listOfSongs = SongListFragment()
    private lateinit var playListFrag: PlaylistFragment

    //    private lateinit var songList: List<Song>
    private lateinit var songTextView: TextView
    private lateinit var playListTextView: TextView
    private val mainViewModel: MainViewModel by viewModels()
    private var curPlayingSong: Song? = null
    private val actionColor:Int=R.color.text_action_color
    private val noActionColor:Int=R.color.text_no_action_color

    @Inject
    lateinit var glide: RequestManager

    //    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        show(View(this))
        songTextView = findViewById(R.id.songs_tv)

        subscribeToObservers()

        btn_play_pause_bottom_menu.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }
    }



    fun show(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songTextView.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor( getColor(noActionColor))
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()

    }


    fun showPlay(view: View) {
        playListFrag = PlaylistFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, playListFrag)
            .commit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songs_tv.setTextColor( getColor(noActionColor))
            playlist_tv.setTextColor(getColor(actionColor))
        }

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

        val view = View(applicationContext)
        glide.load(
            (curPlayingSong ?: songs[0]).imageUrl
        )
            .into(image_bottom_menu)
        tv_song_name_bottom_menu.text =
            (curPlayingSong ?: songs[0].title).toString()


        tv_artist_name_bottom_menu.text =
            (curPlayingSong ?: songs[0].subtitle).toString()
    }

}





