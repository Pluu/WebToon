package com.pluu.webtoon.api;

import android.text.TextUtils;

import java.util.List;

import com.pluu.support.BaseApiImpl;
import com.pluu.support.impl.AbstractEpisodeApi;

/**
 * Created by anchangbeom on 15. 2. 26..
 */
public class WebToon {
	private final BaseApiImpl api;
	private final AbstractEpisodeApi nextApi;
    private final String url;
	public String nextLink;
	public List<Episode> episodes;

    public WebToon(BaseApiImpl api, String url) {
		this.api = api;
		this.nextApi = null;
        this.url = url;
    }

	public WebToon(AbstractEpisodeApi nextApi, String url) {
		this.api = null;
		this.nextApi = nextApi;
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
		if (nextApi != null) {
			return nextApi.moreParseEpisode(this);
		}
		return api.moreParseEpisode(this);
	}
}
