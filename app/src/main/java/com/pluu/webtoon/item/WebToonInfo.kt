package com.pluu.webtoon.item

import android.os.Parcel
import android.os.Parcelable

/**
 * 웹툰 정보 Item Class
 * Created by pluu on 2017-05-04.
 */
class WebToonInfo : BaseToonInfo, Parcelable, Comparable<WebToonInfo> {

    var type: WebToonType? = WebToonType.TOON
    var writer: String? = null
    var isFavorite = false

    constructor(id: String) : super(id)

    constructor(info: BaseToonInfo) : super(info.toonId) {
        title = info.title
    }

    constructor(item: WebToonInfo) : super(item.toonId) {
        title = item.title
        type = item.type
        writer = item.writer
        isFavorite = item.isFavorite
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(type?.ordinal ?: -1)
        dest.writeString(this.writer)
        dest.writeByte(if (isFavorite) 1.toByte() else 0.toByte())
    }

    constructor(source: Parcel) : super(source) {
        val tmpType = source.readInt()
        this.type = if (tmpType == -1) null else WebToonType.values()[tmpType]
        this.writer = source.readString()
        this.isFavorite = source.readByte().toInt() != 0
    }

    override fun compareTo(other: WebToonInfo): Int {
        val BEFORE = -1
        val EQUAL = 0
        val AFTER = 1

        if (this === other) return EQUAL

        // 즐겨찾기
        if (isFavorite && !other.isFavorite) return BEFORE
        if (!isFavorite && other.isFavorite) return AFTER

        // 업데이트 여부 (업데이트)
        if (status === Status.UPDATE && other.status !== Status.UPDATE) return BEFORE
        if (status !== Status.UPDATE && other.status === Status.UPDATE) return AFTER

        // 업데이트 여부 (기본)
        if (status === Status.NONE && other.status !== Status.NONE) return BEFORE
        if (status !== Status.NONE && other.status === Status.NONE) return AFTER

        // 이름
        return title!!.compareTo(other.title!!)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<WebToonInfo> = object : Parcelable.Creator<WebToonInfo> {
            override fun createFromParcel(source: Parcel) = WebToonInfo(source)
            override fun newArray(size: Int): Array<WebToonInfo?> = arrayOfNulls(size)
        }
    }
}
