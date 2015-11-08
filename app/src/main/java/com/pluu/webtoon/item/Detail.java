package com.pluu.webtoon.item;

import java.util.List;

/**
 * 에피소드 상세 내용 Item Class
 * Created by nohhs on 2015-03-12.
 */
public class Detail {
	public String title;
	public String webtoonId, episodeId;
	public String nextLink, prevLink;
	public List<DetailView> list;

	// ERROR_TYPE
	public ERROR_TYPE errorType;
	public String errorMsg;
}
