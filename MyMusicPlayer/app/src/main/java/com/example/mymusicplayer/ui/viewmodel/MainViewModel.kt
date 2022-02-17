package com.example.mymusicplayer.ui.viewmodel

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.data.entetis.Song
import com.example.mymusicplayer.exoplayer.MusicServiceConnection
import com.example.mymusicplayer.exoplayer.isPlayEnabled
import com.example.mymusicplayer.exoplayer.isPlaying
import com.example.mymusicplayer.exoplayer.isPrepared
import com.example.mymusicplayer.other.Constans.MEDIA_ROOT_ID
import com.example.mymusicplayer.other.Resource

class MainViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    private val _mediaItem = MutableLiveData<Resource<List<Song>>>()
    val mediaItem: LiveData<Resource<List<Song>>> = _mediaItem

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val playBackState = musicServiceConnection.playbackState
    val curPlayingSong = musicServiceConnection.curPlayingSong


    init {
        _mediaItem.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val item = children.map {
                        Song(
                            it.mediaId!!,
                            it.description.iconUri.toString(),
                            it.description.subtitle.toString(),
                            it.description.title.toString(),
                            it.description.mediaUri.toString(),
                        )
                    }
                    _mediaItem.postValue(Resource.success(item))
                }
            })
    }


    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()

    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(position: Long) {
        musicServiceConnection.transportControls.seekTo(position)
    }




    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playBackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)
        ) {
            playBackState.value?.let { playBackState ->
                when {
                    playBackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playBackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unSubscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }


}