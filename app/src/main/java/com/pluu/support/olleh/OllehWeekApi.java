package com.pluu.support.olleh;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.api.WebToonType;

/**
 * 올레 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class OllehWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private static final String WEEKLY_URL = "http://webtoon.olleh.com/api/work/getWorkList.kt";
	private static final String[] WEEKLY_VALUE = {"mondayyn", "tuesdayyn", "wednesdayyn", "thursdayyn", "fridayyn", "saturdayyn", "sundayyn"};

	private JSONArray savedArray;
	private int totalSize;

	private final String adult = "adult";
	private final String novel = "novel";

	public OllehWeekApi() {
		super(TITLE);
	}

	@Override
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.OLLEH;
	}

	@Override
	protected int getMainTitleColor(Context context) {
		return R.color.olleh_color;
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();

		try {
			if (savedArray == null) {
				String response = requestApi();
				savedArray = new JSONObject(response).optJSONArray("workList");
				totalSize = savedArray.length();
			}

			WebToonInfo item;
			JSONObject obj;

			StringBuffer writerBuffer = new StringBuffer();
			String temp;
			final String optY = "Y";
			final String optN = "N";

			String checkValue = WEEKLY_VALUE[position];

			for (int i = 0; i < totalSize ; i++) {
				obj = savedArray.optJSONObject(i);
				if (!TextUtils.equals(obj.optString(checkValue), optY)) {
					continue;
				}

				item = new WebToonInfo(obj.optString("webtoonseq"));
				item.setUrl(item.getWebtoonId());
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

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getId() {
		return WEEKLY_URL;
	}

	@Override
	public Map<String, String> getHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("Referer", "http://webtoon.olleh.com");
		return map;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("mobileyn", "N");
		map.put("toonfg", "toon");
		map.put("sort", "subject");
		return map;
	}
}
