package com.pluu.webtoon.api;

import android.text.TextUtils;

import java.util.List;

import com.pluu.support.BaseApiImpl;

/**
 * Created by anchangbeom on 15. 2. 26..
 */
public class WebToon {
	private final BaseApiImpl api;
    private final String url;
	public String nextLink;
	public List<Episode> episodes;

    public WebToon(BaseApiImpl api, String url) {
		this.api = api;
        this.url = url;
    }

	public String getUrl() {
		return url;
	}

	public List<Episode> getEpisodes() {
        return episodes;
    }

	public String getNextLink() {
		return nextLink;
	}

	public String moreLink() {
		if (TextUtils.isEmpty(nextLink)) {
			return null;
		}
		return api.moreParseEpisode(this);
	}
}
