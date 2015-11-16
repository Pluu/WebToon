package com.pluu.webtoon.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 웹툰 정보 Item Class
 * Created by nohhs on 2015-04-07.
 */
public class WebToonInfo extends BaseToonInfo implements Parcelable {

	protected WebToonType type = WebToonType.TOON;
	protected String writer;
	protected boolean favorite = false;

	public WebToonInfo(String id) {
		super(id);
	}

	public WebToonInfo(BaseToonInfo info) {
		super(info.getToonId());
		setTitle(info.getTitle());
	}

	public WebToonInfo(WebToonInfo item) {
		super(item.getToonId());
		setTitle(item.getTitle());
		this.type = item.type;
		this.writer = item.writer;
		this.favorite = item.favorite;
	}

	public WebToonType getType() {
		return type;
	}

	public String getWriter() {
		return writer;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setType(WebToonType type) {
		this.type = type;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.favorite = isFavorite;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.type == null ? -1 : this.type.ordinal());
		dest.writeString(this.writer);
		dest.writeByte(favorite ? (byte) 1 : (byte) 0);
	}

	protected WebToonInfo(Parcel in) {
		super(in);
		int tmpType = in.readInt();
		this.type = tmpType == -1 ? null : WebToonType.values()[tmpType];
		this.writer = in.readString();
		this.favorite = in.readByte() != 0;
	}

	public static final Creator<WebToonInfo> CREATOR = new Creator<WebToonInfo>() {
		public WebToonInfo createFromParcel(Parcel source) {return new WebToonInfo(source);}

		public WebToonInfo[] newArray(int size) {return new WebToonInfo[size];}
	};

	@Override
	public String toString() {
		return "WebToonInfo{" +
			"type=" + type +
			", writer='" + writer + '\'' +
			", favorite=" + favorite +
			'}';
	}
}
