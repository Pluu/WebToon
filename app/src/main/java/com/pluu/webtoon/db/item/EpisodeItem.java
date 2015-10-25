package com.pluu.webtoon.db.item;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.pluu.webtoon.db.Db;
import rx.functions.Func1;
import static com.squareup.sqlbrite.SqlBrite.Query;

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

	public long id;
	public String service;
	public String webtoon;
	public String episode;

	public static final Func1<Query, List<String>> SIMPLE_LIST = new Func1<Query, List<String>>() {
		@Override public List<String> call(Query query) {
			Cursor cursor = query.run();
			try {
				List<String> values = new ArrayList<>(cursor.getCount());
				while (cursor.moveToNext()) {
					String episode = Db.getString(cursor, EPISODE);
					values.add(episode);
				}
				return values;
			} finally {
				cursor.close();
			}
		}
	};

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

		public Builder episode(String episode) {
			values.put(EPISODE, episode);
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
		dest.writeString(this.episode);
	}

	public EpisodeItem() {}

	private EpisodeItem(Parcel in) {
		this.id = in.readLong();
		this.service = in.readString();
		this.webtoon = in.readString();
		this.episode = in.readString();
	}

	public static final Creator<EpisodeItem> CREATOR = new Creator<EpisodeItem>() {
		public EpisodeItem createFromParcel(Parcel source) {return new EpisodeItem(source);}

		public EpisodeItem[] newArray(int size) {return new EpisodeItem[size];}
	};
}
