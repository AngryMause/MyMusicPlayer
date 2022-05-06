package com.example.mymusicplayer.data.entetis

import android.os.Parcel
import android.os.Parcelable

data class Song(
    val mediaId: String? = "",
    val imageUrl: String? = "",
    val subtitle: String? = "",
    val title: String? = "",
    val songUrl: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mediaId)
        parcel.writeString(imageUrl)
        parcel.writeString(subtitle)
        parcel.writeString(title)
        parcel.writeString(songUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}
