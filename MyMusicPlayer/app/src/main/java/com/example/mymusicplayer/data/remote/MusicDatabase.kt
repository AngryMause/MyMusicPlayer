package com.example.mymusicplayer.data.remote

import com.example.mymusicplayer.data.entetis.Song
import com.example.mymusicplayer.other.Constans.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {
    val fireStore = FirebaseFirestore.getInstance()
     val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs():List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }

    }

}