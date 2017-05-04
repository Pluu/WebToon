package com.pluu.webtoon.item

import android.os.Parcel
import android.os.Parcelable

/**
 * 웹툰 기본 정보
 * Created by pluu on 2017-05-04.
 */
open class BaseToonInfo(val toonId: String) : Parcelable {

    var title: String? = null
    var image: String? = null
    var updateDate: String? = null
    var rate: String? = null
    var isAdult = false
    var isLoginNeed = false
    var status: Status? = Status.NONE

    val isLock: Boolean
        get() = isLoginNeed || isAdult

    constructor(item: BaseToonInfo) : this(item.toonId) {
        this.title = item.title
        this.image = item.image
        this.updateDate = item.updateDate
        this.rate = item.rate
        this.isAdult = item.isAdult
        this.isLoginNeed = item.isLoginNeed
        this.status = item.status
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BaseToonInfo> = object : Parcelable.Creator<BaseToonInfo> {
            override fun createFromParcel(source: Parcel): BaseToonInfo = BaseToonInfo(source)
            override fun newArray(size: Int): Array<BaseToonInfo?> = arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0

    constructor(source: Parcel) : this(source.readString()) {
        title = source.readString()
        image = source.readString()
        updateDate = source.readString()
        rate = source.readString()
        isAdult = source.readByte() != 0.toByte()
        isLoginNeed = source.readByte() != 0.toByte()
        val tmpStatus = source.readInt()
        status = if (tmpStatus == -1) null else Status.values()[tmpStatus]
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.toonId)
        dest.writeString(this.title)
        dest.writeString(this.image)
        dest.writeString(this.updateDate)
        dest.writeString(this.rate)
        dest.writeByte(if (isAdult) 1 else 0)
        dest.writeByte(if (isLoginNeed) 1 else 0)
        dest.writeInt(status?.ordinal ?: -1)
    }
}
