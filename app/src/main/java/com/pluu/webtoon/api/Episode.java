package com.pluu.webtoon.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 에피소드 Item Class
 * Created by anchangbeom on 15. 2. 26..
 */
public class Episode extends WebToonInfo implements Parcelable {
	private String episodeId;
	private String episodeTitle;
	private boolean isLoginNeed = false;
	private boolean isLocked = false;
	private boolean isReaded;
	private Object tag;

	public Episode(WebToonInfo info, String episodeId) {
		super(info);
		this.episodeId = episodeId;
	}

	public String getEpisodeId() {
		return episodeId;
	}

	public String getEpisodeTitle() {
		return episodeTitle;
	}

	public boolean isLoginNeed() {
		return isLoginNeed;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}

	public void setIsLoginNeed(boolean isLoginNeed) {
		this.isLoginNeed = isLoginNeed;
	}

	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public void setIsReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	@Override
	public Episode clone() throws CloneNotSupportedException {
		return new Episode(this);
	}

	public Episode(Episode item) {
		super(item);
		this.episodeId = item.episodeId;
		this.episodeTitle = item.episodeTitle;
		this.isLoginNeed = item.isLoginNeed;
		this.isLocked = item.isLocked;
		this.isReaded = item.isReaded;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.episodeId);
		dest.writeString(this.episodeTitle);
		dest.writeByte(isLoginNeed ? (byte) 1 : (byte) 0);
		dest.writeByte(isLocked ? (byte) 1 : (byte) 0);
		dest.writeByte(isReaded ? (byte) 1 : (byte) 0);
	}

	private Episode(Parcel in) {
		super(in);
		this.episodeId = in.readString();
		this.episodeTitle = in.readString();
		this.isLoginNeed = in.readByte() != 0;
		this.isLocked = in.readByte() != 0;
		this.isReaded = in.readByte() != 0;
	}

	public static final Creator<Episode> CREATOR = new Creator<Episode>() {
		public Episode createFromParcel(Parcel source) {return new Episode(source);}

		public Episode[] newArray(int size) {return new Episode[size];}
	};
}
