package com.pluu.webtoon.db.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Episode DB Item
 * Created by nohhs on 2015-03-17.
 */
public class EpisodeItem implements Parcelable {
	public static final String TABLE = "episode_item";

	public static final String ID = "_id";
	public static final String SERVICE = "service";
	public static final String WEBTOON = "webtoon";
	public static final String EPISODE = "episode";

	public String service;
	public String webtoon;
	public String episode;

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.service);
		dest.writeString(this.webtoon);
		dest.writeString(this.episode);
	}

	public EpisodeItem() {}

	private EpisodeItem(Parcel in) {
		this.service = in.readString();
		this.webtoon = in.readString();
		this.episode = in.readString();
	}

	public static final Creator<EpisodeItem> CREATOR = new Creator<EpisodeItem>() {
		public EpisodeItem createFromParcel(Parcel source) {return new EpisodeItem(source);}

		public EpisodeItem[] newArray(int size) {return new EpisodeItem[size];}
	};
}
