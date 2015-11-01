package com.pluu.support.olleh;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.BaseApiImpl;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.api.WebToonType;
import org.jsoup.Connection;

/**
 * 올레 웹툰 Parse Api
 * Created by nohhs on 2015-03-13.
 */
public class OllehApi extends BaseApiImpl {

	private final int PAGE_SIZE = 20;

	private final String[] weekValue = {"mondayyn", "tuesdayyn", "wednesdayyn", "thursdayyn", "fridayyn", "saturdayyn", "sundayyn"};
	private final String WEEKLY_URL = "http://webtoon.olleh.com/api/work/getWorkList.kt";
	private final String EPISODE_URL = "http://webtoon.olleh.com/api/work/getTimesListByWork.kt";
	private final String DETAIL_URL = "http://webtoon.olleh.com/api/work/getTimesListByWork.kt";
	private final String DETAIL_IMG_URL = "http://webtoon.olleh.com/api/work/getTimesDetailImageList.kt";
//	private final String DETAIL_NOVEL_URL = "http://m.webtoon.olleh.com/m/novel/novelJsonDetail.kt?webtoonseq=%d&timesseq=%d";

	private final String adult = "adult";
	private final String novel = "novel";

	private JSONArray savedArray;
	private int totalSize;

	private Episode firstEpisode;
	private int page = 0;

	public OllehApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일"});
	}

	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.OLLEH;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.olleh_color);
	}

	public String getWeeklyUrl(int position) {
		return WEEKLY_URL;
	}

	@Override
	protected Connection getConnection(String url) {
		Connection connection = super.getConnection(url);
		connection.header("Referer", "http://webtoon.olleh.com");
		connection.data("mobileyn", "N");
		return connection;
	}

	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			if (savedArray == null) {
				String jsonData = getConnection(url)
					.data("toonfg", "toon")
					.data("sort", "subject")
					.ignoreContentType(true)
					.execute().body();
				savedArray = new JSONObject(jsonData).optJSONArray("workList");
				totalSize = savedArray.length();
			}

			WebToonInfo item;
			JSONObject obj;

			StringBuffer writerBuffer = new StringBuffer();
			String temp;
			final String optY = "Y";
			final String optN = "N";

			String checkValue = weekValue[position];

			for (int i = 0; i < totalSize ; i++) {
				obj = savedArray.optJSONObject(i);
				if (!TextUtils.equals(obj.optString(checkValue), optY)) {
					continue;
				}

				item = new WebToonInfo(obj.optString("webtoonseq"));
				item.setUrl(EPISODE_URL);
				item.setTitle(obj.optString("webtoonnm"));
				item.setImage(obj.optString("newthumbpath"));

				writerBuffer.setLength(0);
				writerBuffer.append(obj.optString("authornm1"));

				temp = obj.optString("authornm2");
				if (!TextUtils.isEmpty(temp)) {
					writerBuffer.append(", ").append(temp);
				}
				temp = obj.optString("authornm3");
				if (!TextUtils.isEmpty(temp)) {
					writerBuffer.append(", ").append(temp);
				}
				item.setWriter(writerBuffer.toString());
				item.setRate(obj.optString("totalstickercnt"));
				item.setUpdateDate(obj.optString("regdt"));
				if (optN.equals(obj.optString("endyn", optN))) {
					if (optY.equals(obj.optString("upyn", optN))) {
						// 최근 업데이트
						item.setStatus(Status.UPDATE);
					} else if (optY.equals(obj.optString("restyn", optN))){
						// 휴재
						item.setStatus(Status.BREAK);
					}
				}

				if (adult.equals(obj.optString("agefg"))) {
					item.setIsAdult(true);
				}

				if (novel.equals(obj.optString("toonfg"))) {
					item.setType(WebToonType.NOVEL);
				}

				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		WebToon webToon = new WebToon(this, url);
		try {
			if (savedArray == null) {
				String jsonData = getConnection(url)
					.data("webtoonseq", info.getWebtoonId())
					.ignoreContentType(true)
					.execute().body();
				JSONObject obj = new JSONObject(jsonData);
				savedArray = obj.optJSONArray("timesList");

				if (savedArray != null) {
					totalSize = savedArray.length();
					firstEpisode
						= createEpisode(info, savedArray.optJSONObject(totalSize - 1));
				}
			}

			if (totalSize > 0) {
				webToon.episodes = parseList(info, savedArray, page);
				webToon.nextLink = getNextPageLink(totalSize, page);
			}
			page++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray array, int page) {
		List<Episode> list = new ArrayList<>();

		int startPage = page * PAGE_SIZE;
		int endPage = (page + 1) * PAGE_SIZE;
		if (endPage > totalSize) {
			endPage = totalSize;
		}

		for (int i = startPage; i < endPage; i++) {
			list.add(createEpisode(info, array.optJSONObject(i)));
		}
		return list;
	}

	private Episode createEpisode(WebToonInfo info, JSONObject obj) {
		Episode item = new Episode(info, obj.optString("timesseq"));
		item.setUrl(DETAIL_IMG_URL);
		item.setEpisodeTitle(obj.optString("timestitle"));
		item.setImage(info.getType() == WebToonType.TOON
						  ? obj.optString("thumbpath")
						  : info.getImage());
		item.setRate(obj.optString("totalstickercnt"));
//				item.setUpdateDate(obj.optString("regdt"));

		return item;
	}

	private String getNextPageLink(int totalSize, int current) {
		int total = (int) Math.ceil(totalSize / (double) PAGE_SIZE);
		if (total >= current + 1) {
			// 다음 페이지 존재
			return EPISODE_URL;
		}

		return null;
	}

	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();

		JSONObject obj;
		List<DetailView> list = new ArrayList<>();
		try {
			String paramEpisodeId = episode.getTag() != null
				? String.valueOf(episode.getTag()) : episode.getEpisodeId();

			String jsonData = getConnection(DETAIL_URL)
				.ignoreContentType(true)
				.data("webtoonseq", episode.getWebtoonId())
				.data("timesseq", paramEpisodeId)
				.execute().body();

			obj = new JSONObject(jsonData);

			JSONArray array = obj.optJSONArray("timesList");
			int timeSeq = Integer.valueOf(paramEpisodeId);
			for (int i = 0; i < array.length(); i++) {
				obj = array.optJSONObject(i);
				if (timeSeq == obj.optInt("timesseq")) {
					ret.episodeId = obj.optString("timesseq");
					ret.title = obj.optString("timestitle");
					if (i - 1 >= 0) {
						ret.nextLink = DETAIL_URL;
						ret.nextIdx = array.optJSONObject(i - 1).optInt("timesseq");
					}
					if (i + 1 < array.length()) {
						ret.prevLink = DETAIL_URL;
						ret.prevIdx = array.optJSONObject(i + 1).optInt("timesseq");
					}
					break;
				}
			}

			jsonData = getConnection(DETAIL_IMG_URL)
				.ignoreContentType(true)
				.data("webtoonseq", episode.getWebtoonId())
				.data("timesseq", paramEpisodeId)
				.execute().body();
			obj = new JSONObject(jsonData);
			parserToon(ret, list, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.list = list;
		return ret;
	}

	private void parserToon(Detail ret, List<DetailView> list, JSONObject obj) {
		JSONArray array = obj.optJSONArray("imageList");
		JSONObject obj2;
		for (int i = 0; i < array.length(); i++) {
			obj2 = array.optJSONObject(i);
			list.add(DetailView.createImage(obj2.optString("imagepath")));
		}
	}

	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	public ShareItem getDetailShare(Episode episode, Detail detail) {
		String url = String.format("http://webtoon.olleh.com/web/times_view.kt?webtoonseq=%s&timesseq=%s",
								   detail.webtoonId, detail.episodeId);
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = url;
		return item;
	}
}
