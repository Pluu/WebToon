package com.pluu.webtoon.item

/**
 * 에피소드 상세 내용 Item Class
 * Created by pluu on 2017-05-02.
 */
class Detail {
    var title: String? = null
    var webtoonId: String? = null
    var episodeId: String? = null
    var nextLink: String? = null
    var prevLink: String? = null
    var list: List<DetailView>? = null

    // View Type
    var type = DETAIL_TYPE.DEFAULT

    // ERROR_TYPE
    var errorType: ERROR_TYPE? = null
    var errorMsg: String? = null
}
