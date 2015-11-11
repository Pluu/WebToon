package com.pluu.webtoon.model;

import com.pluu.webtoon.db.item.EpisodeItem;
import io.realm.RealmObject;

/**
 * 에피소드 Model
 * Created by PLUUSYSTEM-NEW on 2015-11-11.
 */
public class REpisode extends RealmObject {
	private String service;
	private String toonId;
	private String episodeId;

	public REpisode() { }

	public REpisode(EpisodeItem item) {
		this.service = item.service;
		this.toonId = item.webtoon;
		this.episodeId = item.episode;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setToonId(String toonId) {
		this.toonId = toonId;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public String getService() {
		return service;
	}

	public String getToonId() {
		return toonId;
	}

	public String getEpisodeId() {
		return episodeId;
	}
}
