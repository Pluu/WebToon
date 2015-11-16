package com.pluu.webtoon.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 에피소드 Item Class
 * Created by nohhs on 2015-04-06.
 */
public class Episode extends BaseToonInfo implements Parcelable {
	private String episodeId;
	private String episodeTitle;
	private boolean readFlag;

	public Episode(BaseToonInfo info, String episodeId) {
		super(info);
		this.episodeId = episodeId;
	}

	public Episode(Episode item) {
		super(item);
		this.episodeId = item.episodeId;
		this.episodeTitle = item.episodeTitle;
		this.readFlag = item.readFlag;
	}

	public String getEpisodeId() {
		return episodeId;
	}

	public String getEpisodeTitle() {
		return episodeTitle;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}

	public void setReadFlag() {
		this.readFlag = true;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.episodeId);
		dest.writeString(this.episodeTitle);
		dest.writeByte(readFlag ? (byte) 1 : (byte) 0);
	}

	private Episode(Parcel in) {
		super(in);
		this.episodeId = in.readString();
		this.episodeTitle = in.readString();
		this.readFlag = in.readByte() != 0;
	}

	public static final Creator<Episode> CREATOR = new Creator<Episode>() {
		public Episode createFromParcel(Parcel source) {return new Episode(source);}

		public Episode[] newArray(int size) {return new Episode[size];}
	};
}
