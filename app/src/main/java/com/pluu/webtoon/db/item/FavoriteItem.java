package com.pluu.webtoon.db.item;

import android.content.ContentValues;
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

	public long id;
	public String service;
	public String webtoon;
	public int favorite;

	public static final class Builder {
		private final ContentValues values = new ContentValues();

		public Builder id(long id) {
			values.put(ID, id);
			return this;
		}

		public Builder service(String service) {
			values.put(SERVICE, service);
			return this;
		}

		public Builder webtoon(String webtoon) {
			values.put(WEBTOON, webtoon);
			return this;
		}

		public Builder favorite(boolean favorite) {
			values.put(FAVORITE, favorite ? 1 : 0);
			return this;
		}

		public ContentValues build() {
			return values;
		}
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.service);
		dest.writeString(this.webtoon);
		dest.writeInt(this.favorite);
	}

	public FavoriteItem() {}

	private FavoriteItem(Parcel in) {
		this.id = in.readLong();
		this.service = in.readString();
		this.webtoon = in.readString();
		this.favorite = in.readInt();
	}

	public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
		public FavoriteItem createFromParcel(Parcel source) {return new FavoriteItem(source);}

		public FavoriteItem[] newArray(int size) {return new FavoriteItem[size];}
	};
}
