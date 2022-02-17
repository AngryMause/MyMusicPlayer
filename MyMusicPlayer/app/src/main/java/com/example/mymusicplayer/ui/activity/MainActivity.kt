package com.example.mymusicplayer.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.exoplayer.FireBaseMusicSours
import com.example.mymusicplayer.ui.fragment.PlaylistFragment
import com.example.mymusicplayer.ui.fragment.SongListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val listOfSongs = SongListFragment()
    private val playlistFragment = PlaylistFragment
    private val playMusicFragment = PlaylistFragment()


    private lateinit var sours: FireBaseMusicSours


    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()
    }

    fun show(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, listOfSongs)
            .commit()
    }

    fun showPlay(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, playMusicFragment)
            .commit()
    }


//   private fun showArtistFragment(view: View) {
//       supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, )
//           .commit()
//    }
//    private fun showAlbumFragment(view: View) {
//
//        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, mainFrgament)
//            .commit()
//    }

}




