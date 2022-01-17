package com.pluu.webtoon.model

sealed class DetailResult {

    /**
     * 에피소드 상세 내용 Item Class
     * Created by pluu on 2018. 10. 07..
     */
    class Detail(
        val webtoonId: ToonId,
        val episodeId: EpisodeId,
        val title: String
    ) : DetailResult() {
        var nextLink: String? = null
        var prevLink: String? = null
        var list: List<DetailView> = emptyList()
    }

    /**
     * 에피소드 에러 Class
     * Created by pluu on 2018. 10. 07..
     */
    class ErrorResult(
        val errorType: ERROR_TYPE
    ) : DetailResult()
}
