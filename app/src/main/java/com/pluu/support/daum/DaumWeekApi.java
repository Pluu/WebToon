package com.pluu.support.daum;

import android.content.Context;
import android.text.TextUtils;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.item.BaseToonInfo;
import com.pluu.webtoon.item.Status;
import com.pluu.webtoon.item.WebToonInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 다음 웹툰 Week Api
 * Created by PLUUSYSTEM-NEW on 2015-10-30.
 */
public class DaumWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private static final String URL = "http://m.webtoon.daum.net/data/mobile/webtoon";
	private final String[] URL_VALUE = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};

	private int currentPos;

	public DaumWeekApi(Context context) {
		super(context, TITLE);
	}

	@Override
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.DAUM;
	}

	@Override
	public List<WebToonInfo> parseMain(int position) {
		this.currentPos = position;

		ArrayList<WebToonInfo> list = new ArrayList<>();

		try {
			String response = requestApi();
			JSONArray array = new JSONObject(response)
				.optJSONObject("data").optJSONArray("webtoons");
			if (array != null && array.length() > 0) {
				JSONObject obj, lastObj;
				String emptyAverageScore = "0.0";
				String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
				String date;
				for (int i = 0; i < array.length(); i++) {
					obj = array.optJSONObject(i);

					BaseToonInfo baseInfo = new BaseToonInfo(obj.optString("nickname"));
					baseInfo.setTitle(obj.optString("title"));

					WebToonInfo item = new WebToonInfo(baseInfo);
					lastObj = obj.optJSONObject("latestWebtoonEpisode");
					item.setImage(lastObj.optJSONObject("thumbnailImage").optString("url"));

					JSONObject info = obj.optJSONObject("cartoon").optJSONArray("artists").optJSONObject(0);
					item.setWriter(info.optString("name"));
					item.setRate(Const.getRateNameByRate(obj.optString("averageScore")));
					if (TextUtils.equals(emptyAverageScore, item.getRate())) {
						item.setRate(null);
					}

					date = lastObj.optString("dateCreated");
					item.setUpdateDate(
						date.substring(2, 4) + "." + date.substring(4, 6) + "." + date.substring(6,
																								 8));
					if (today.equals(date.substring(0, 8))) {
						// 최근 업데이트
						item.setStatus(Status.UPDATE);
					} else if ("Y".equals(obj.optString("restYn"))){
						// 휴재
						item.setStatus(Status.BREAK);
					}

					item.setIsAdult(obj.optInt("ageGrade") == 19);
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getUrl() {
		return URL;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("sort", "update");
		map.put("page_no", "1");
		map.put("week", URL_VALUE[currentPos]);
		return map;
	}

}
