package com.pluu.webtoon.db;

import android.database.Cursor;
import android.util.Log;

import java.util.List;

import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by nohhs on 2015-05-31.
 */
public class InjectDB {

	private static final String TAG = InjectDB.class.getSimpleName();

	private static final String LIST_QUERY = "SELECT " + EpisodeItem.EPISODE + " FROM "
		+ EpisodeItem.TABLE
		+ " WHERE "
		+ EpisodeItem.SERVICE + " = ? AND "
		+ EpisodeItem.WEBTOON + " = ? "
		+ " ORDER BY "
		+ EpisodeItem.EPISODE + " ASC";

	private static final String DETAIL_QUERY = "SELECT " + EpisodeItem.ID + " FROM "
		+ EpisodeItem.TABLE
		+ " WHERE "
		+ EpisodeItem.SERVICE + " = ? AND "
		+ EpisodeItem.WEBTOON + " = ? AND "
		+ EpisodeItem.EPISODE + " = ?";

	private static final String FAVORITE_QUERY = "SELECT " + FavoriteItem.ID + " FROM "
		+ FavoriteItem.TABLE
		+ " WHERE "
		+ FavoriteItem.SERVICE + " = ? AND "
		+ FavoriteItem.WEBTOON + " = ?";

	private static final String FAVORITE_DELETE =
		FavoriteItem.SERVICE + " = ? AND "
			+ FavoriteItem.WEBTOON + " = ?";

	public static
	Subscription getEpisodeInfo(final BriteDatabase db,
								final String apiTag,
								final WebToonInfo info,
								Action1<List<String>> onAction) {

		return db.createQuery(EpisodeItem.TABLE, LIST_QUERY,
							  apiTag,
							  info.getWebtoonId())

				 .map(EpisodeItem.SIMPLE_LIST)
				 .subscribeOn(Schedulers.io())
				 .observeOn(AndroidSchedulers.mainThread())
				 .subscribe(onAction);
	}

	public static
	void updateDetail(final BriteDatabase db,
					  final String apiTag,
					  final Detail item) {
		db.createQuery(EpisodeItem.TABLE, DETAIL_QUERY,
					   apiTag,
					   item.webtoonId,
					   item.episodeId)
		  .map(new Func1<SqlBrite.Query, Boolean>() {
			  @Override
			  public Boolean call(SqlBrite.Query query) {
				  Cursor cursor = query.run();
				  return cursor.getCount() > 0;
			  }
		  })
		  .subscribeOn(Schedulers.io())
		  .observeOn(AndroidSchedulers.mainThread())
		  .subscribe(new Action1<Boolean>() {
			  @Override
			  public void call(Boolean value) {
				  if (value) {
					  return;
				  }

				  db.insert(EpisodeItem.TABLE,
							new EpisodeItem.Builder()
								.service(apiTag)
								.webtoon(item.webtoonId)
								.episode(item.episodeId)
								.build());
			  }
		  });
	}

	public static
	Subscription getEpisodeFavorite(final BriteDatabase db,
									final String apiTag,
									final WebToonInfo info,
									Action1<Boolean> onAction) {

//		Log.d(TAG, "getEpisodeFavorite, " + info.toString());

		return db.createQuery(
			FavoriteItem.TABLE, FAVORITE_QUERY,
			apiTag,
			info.getWebtoonId())
				 .map(new Func1<SqlBrite.Query, Boolean>() {
					 @Override
					 public Boolean call(SqlBrite.Query query) {
						 Cursor cursor = query.run();
						 return cursor.getCount() > 0;
					 }
				 })
				 .subscribeOn(Schedulers.io())
				 .observeOn(AndroidSchedulers.mainThread())
				 .subscribe(onAction);
	}

	public static
	Subscription favoriteAdd(final BriteDatabase db,
							 final String apiTag,
							 final WebToonInfo item) {

		Log.d(TAG, "favoriteAdd, " + item.toString());

		return db.createQuery(
			FavoriteItem.TABLE, FAVORITE_QUERY,
			apiTag,
			item.getWebtoonId())
				 .map(new Func1<SqlBrite.Query, Boolean>() {
					 @Override
					 public Boolean call(SqlBrite.Query query) {
						 Cursor cursor = query.run();
						 return cursor.getCount() > 0;
					 }
				 })
				 .subscribeOn(Schedulers.io())
				 .observeOn(AndroidSchedulers.mainThread())
				 .subscribe(new Action1<Boolean>() {
					 @Override
					 public void call(Boolean value) {
						 if (value) {
							 return;
						 }

						 db.insert(
							 FavoriteItem.TABLE,
							 new FavoriteItem.Builder()
								 .service(apiTag)
								 .webtoon(item.getWebtoonId())
								 .favorite(true)
								 .build());
					 }
				 });

	}

	public static
	int favoriteDelete(final BriteDatabase db,
					   final String apiTag,
					   final WebToonInfo item) {
		Log.d(TAG, "favoriteDelete, " + item.toString());

		return db.delete(FavoriteItem.TABLE,
						 FAVORITE_DELETE,
						 apiTag,
						 item.getWebtoonId());
	}

}
