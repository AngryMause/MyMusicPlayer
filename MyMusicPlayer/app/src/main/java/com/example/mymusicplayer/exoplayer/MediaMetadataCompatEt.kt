package com.example.mymusicplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.mymusicplayer.data.entetis.Song

fun MediaMetadataCompat.toSong(): Song? {
    return description?.let {
        Song(
            it.mediaId ?: String(),
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}