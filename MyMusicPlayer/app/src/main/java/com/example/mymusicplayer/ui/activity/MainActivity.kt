package com.example.mymusicplayer.ui.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.exoplayer.isPlaying
import com.example.mymusicplayer.exoplayer.toSong
import com.example.mymusicplayer.model.SongModel
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
    private var playBackState: PlaybackStateCompat? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var curPlayingSong: SongModel? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val actionColor: Int = R.color.text_action_color
    private val noActionColor: Int = R.color.text_not_in_focus
    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.isGone = true
        playMusicFragment = PlayMusicFragment()
        subscribeToObservers()
        showlistOfSongFragment()
        btn_play_pause_bottom_menu.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_navigation)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottom_layout_current_song.scaleX = 1F - slideOffset
                bottom_layout_current_song.scaleY = 1F - slideOffset
                //                tv_song_name_bottom_menu.scaleX =
//                    (1 + ((maxScale - minScale) * slideOffset)).toFloat()
//                tv_song_name_bottom_menu.scaleY =
//                    (1 + ((maxScale - minScale) * slideOffset)).toFloat()
//                tv_song_name_bottom_menu.translationX = -leftMoveDistance * slideOffset
//                image_bottom_menu.alpha = 1 - slideOffset / 1.5f
            }
        })

        tv_search.addTextChangedListener{ text: Editable? ->
            listOfSongs.filter(text.toString())
        }
    }




    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(this) {
            it.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        bottom_navigation.isGone = false
                        result.data?.let { songs ->
                            if (songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                            }
                        }
                    }
                    ERROR -> Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                    LOADING -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(this) {
            curPlayingSong = it.toSong()
            setDataToBottomVIew(curPlayingSong!!)
            //            if (it ==null) return@observe


        }
        chengPlayPauseIcon()

    }

    private fun chengPlayPauseIcon() {
        mainViewModel.playBackState.observe(this) {
            playBackState = it
            btn_play_pause_bottom_menu.setImageResource(
                if (playBackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.exo_icon_play
            )
        }
    }


    private fun setDataToBottomVIew(songs: SongModel) {
        glide.load(songs.imageUrl).into(image_bottom_menu)
        tv_song_name_bottom_menu.text = songs.title
        tv_artist_name_bottom_menu.text = songs.subtitle

    }


    fun showListFragment(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songs_tv.setTextColor(getColor(actionColor))
            playlist_tv.setTextColor(getColor(noActionColor))
            album_tv.setTextColor(getColor(noActionColor))
            artist_tv.setTextColor(getColor(noActionColor))
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()

    }

    private fun showlistOfSongFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            songs_tv.setTextColor(getColor(actionColor))
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
        artistFragment = ArtistFragment()
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
        albumFragment = AlbumFragment()
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


}





