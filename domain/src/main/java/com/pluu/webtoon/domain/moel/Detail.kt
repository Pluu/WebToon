package com.pluu.webtoon.domain.moel

sealed class DetailResult {

    /**
     * 에피소드 상세 내용 Item Class
     * Created by pluu on 2018. 10. 07..
     */
    class Detail(
        var webtoonId: String,
        var episodeId: String
    ) : DetailResult() {
        var title: String? = null
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
