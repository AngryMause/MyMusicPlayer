package com.example.mymusicplayer.ui.fragment

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.exoplayer.isPlaying
import com.example.mymusicplayer.exoplayer.toSong
import com.example.mymusicplayer.model.SongModel
import com.example.mymusicplayer.other.Status.SUCCESS
import com.example.mymusicplayer.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.flragment_play_music.*
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class PlayMusicFragment : BaseFragment(R.layout.flragment_play_music) {

    @Inject
    lateinit var glide: RequestManager
    private var curPlayingSong: SongModel? = null
    private val songViewModel: SongViewModel by viewModels()
    private var playbackStateCompat: PlaybackStateCompat? = null
    private var shouldUpdateSeekBar = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserver()
        play_pause_song_iv.setOnClickListener {
            mainViewModel.playOrToggleSong(curPlayingSong!!, true)
        }
        previous_song_tv.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }
        next_song_tv.setOnClickListener {
            mainViewModel.skipToNextSong()
        }
        seekBar()
    }

    private fun seekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progres: Int, p2: Boolean) {
                if (p2) {
                    setCurrentPlayerToTextView(progres.toLong())
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                shouldUpdateSeekBar = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekBar = true
                }
            }
        })

    }

    private fun subscribeToObserver() {
        mainViewModel.mediaItem.observe(viewLifecycleOwner) {
            it.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if (songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAmdSongImage(curPlayingSong!!)
        }
        songViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekBar) {
                seekBar.progress = it.toInt()
                setCurrentPlayerToTextView(it)
            }
        }
        songViewModel.curSongDuration.observe(viewLifecycleOwner) {
            seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss")
            total_time_song_tv.text = dateFormat.format(it)

        }
        chengIconPlayPauseButton()
    }

    private fun setCurrentPlayerToTextView(it: Long?) {
        val dateFormat = SimpleDateFormat("mm:ss")
        cur_time_song_tv.text = dateFormat.format(it)
    }

    private fun chengIconPlayPauseButton() {
        mainViewModel.playBackState.observe(viewLifecycleOwner) {
            playbackStateCompat = it
            play_pause_song_iv.setImageResource(
                if (playbackStateCompat?.isPlaying == true) R.drawable.ic_pause_large else R.drawable.exo_icon_play
            )
        }
    }


    private fun updateTitleAmdSongImage(song: SongModel) {
        tv_title_song_play_song.text = song.title
        glide.load(song.imageUrl).into(im_current_image_playing_backgraund)
        tv_subtitle_song.text = song.subtitle
        glide.load(song.imageUrl).into(im_song_image)
    }


}