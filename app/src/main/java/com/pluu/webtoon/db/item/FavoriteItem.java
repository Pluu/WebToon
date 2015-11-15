package com.pluu.webtoon.db.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Favorite Item
 * Created by nohhs on 2015-05-31.
 */
public class FavoriteItem implements Parcelable {
	public static final String TABLE = "favorite_item";

	public static final String ID = "_id";
	public static final String SERVICE = "service";
	public static final String WEBTOON = "webtoon";
	public static final String FAVORITE = "favorite";

	public String service;
	public String webtoon;
	public int favorite;

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.service);
		dest.writeString(this.webtoon);
		dest.writeInt(this.favorite);
	}

	public FavoriteItem() {}

	private FavoriteItem(Parcel in) {
		this.service = in.readString();
		this.webtoon = in.readString();
		this.favorite = in.readInt();
	}

	public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
		public FavoriteItem createFromParcel(Parcel source) {return new FavoriteItem(source);}

		public FavoriteItem[] newArray(int size) {return new FavoriteItem[size];}
	};
}
