package com.example.mymusicplayer.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.entetis.Song
import com.example.mymusicplayer.exoplayer.toSong
import com.example.mymusicplayer.other.Status.*
import com.example.mymusicplayer.ui.fragment.*
import com.example.mymusicplayer.ui.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.botom_curren_song_navigation.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val listOfSongs = SongListFragment()
    private lateinit var playListFrag: PlaylistFragment
    private lateinit var playMusicFragment: PlayMusicFragment
    private lateinit var artistFragment: ArtistFragment
    private lateinit var albumFragment: AlbumFragment
    private lateinit var songTextView: TextView
    private lateinit var playListTextView: TextView
    private val mainViewModel: MainViewModel by viewModels()
    private var curPlayingSong: Song? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val actionColor: Int = R.color.text_action_color
    private val noActionColor: Int = R.color.text_no_action_color

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songTextView = findViewById(R.id.songs_tv)
        playMusicFragment = PlayMusicFragment()
        subscribeToObservers()
        showlistOfSongFargment()

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_navigation)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottom_current_song.scaleX = 1F - slideOffset
                bottom_current_song.scaleY = 1F - slideOffset
                //                tv_song_name_bottom_menu.scaleX =
//                    (1 + ((maxScale - minScale) * slideOffset)).toFloat()
//                tv_song_name_bottom_menu.scaleY =
//                    (1 + ((maxScale - minScale) * slideOffset)).toFloat()
//                tv_song_name_bottom_menu.translationX = -leftMoveDistance * slideOffset
//                image_bottom_menu.alpha = 1 - slideOffset / 1.5f
            }
        })
        btn_play_pause_bottom_menu.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }
    }

    fun showListFargment(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songTextView.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor(getColor(noActionColor))
            album_tv.setTextColor(getColor(noActionColor))
            artist_tv.setTextColor(getColor(noActionColor))
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()

    }

    private fun showlistOfSongFargment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songTextView.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor(getColor(noActionColor))
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .addToBackStack(null)
            .commit()
    }


    fun showPlayListFragment(view: View) {
        playListFrag = PlaylistFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, playListFrag)
            .addToBackStack(null)
            .commit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            playlist_tv.setTextColor(getColor(actionColor))
            songs_tv.setTextColor(getColor(noActionColor))
            album_tv.setTextColor(getColor(noActionColor))
            artist_tv.setTextColor(getColor(noActionColor))
        }

    }

    fun showArtistFragment(view: View) {
        artistFragment=ArtistFragment()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            artist_tv.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor(getColor(noActionColor))
            album_tv.setTextColor(getColor(noActionColor))
            songs_tv.setTextColor(getColor(noActionColor))

        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, artistFragment)
            .addToBackStack(null)
            .commit()
    }
    fun showAlbumFragment(view: View) {
        albumFragment= AlbumFragment()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            album_tv.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor(getColor(noActionColor))
            artist_tv.setTextColor(getColor(noActionColor))
            songs_tv.setTextColor(getColor(noActionColor))

        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, albumFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(this) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
//                            if (songs.isNotEmpty()) {
//                                setDataToBottomVIew(songs)
//                            }
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







}





