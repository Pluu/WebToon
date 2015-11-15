package com.pluu.webtoon.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 에피소드 Item Class
 * Created by nohhs on 2015-04-06.
 */
public class Episode extends WebToonInfo implements Parcelable {
	private String episodeId;
	private String episodeTitle;
	private boolean readed;

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

	public boolean isReaded() {
		return readed;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}

	public void setReadFlag() {
		this.readed = true;
	}

	@Override
	public Episode clone() throws CloneNotSupportedException {
		return new Episode(this);
	}

	public Episode(Episode item) {
		super(item);
		this.episodeId = item.episodeId;
		this.episodeTitle = item.episodeTitle;
		this.readed = item.readed;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.episodeId);
		dest.writeString(this.episodeTitle);
		dest.writeByte(readed ? (byte) 1 : (byte) 0);
	}

	private Episode(Parcel in) {
		super(in);
		this.episodeId = in.readString();
		this.episodeTitle = in.readString();
		this.readed = in.readByte() != 0;
	}

	public static final Creator<Episode> CREATOR = new Creator<Episode>() {
		public Episode createFromParcel(Parcel source) {return new Episode(source);}

		public Episode[] newArray(int size) {return new Episode[size];}
	};
}
