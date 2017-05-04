package com.pluu.webtoon.item

import android.os.Parcel
import android.os.Parcelable

/**
 * 에피소드 Item Class
 * Created by pluu on 2017-05-04.
 */
class Episode : BaseToonInfo, Parcelable {
    var episodeId: String
    var episodeTitle: String? = null
    var isReadFlag: Boolean = false
        private set

    constructor(info: BaseToonInfo, episodeId: String) : super(info) {
        this.episodeId = episodeId
    }

    constructor(item: Episode) : super(item) {
        episodeId = item.episodeId
        episodeTitle = item.episodeTitle
        isReadFlag = item.isReadFlag
    }

    fun setReadFlag() {
        this.isReadFlag = true
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(this.episodeId)
        dest.writeString(this.episodeTitle)
        dest.writeByte(if (isReadFlag) 1.toByte() else 0.toByte())
    }

    private constructor(source: Parcel) : super(source) {
        episodeId = source.readString()
        episodeTitle = source.readString()
        isReadFlag = source.readByte().toInt() != 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Episode> = object : Parcelable.Creator<Episode> {
            override fun createFromParcel(source: Parcel) = Episode(source)
            override fun newArray(size: Int): Array<Episode?> = arrayOfNulls(size)
        }
    }
}
