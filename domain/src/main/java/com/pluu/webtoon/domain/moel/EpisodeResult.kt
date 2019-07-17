package com.pluu.webtoon.domain.moel

/**
 * EpisodeInfo Page Info Class
 * Created by pluu on 2017-05-02.
 */
class EpisodeResult(
    val episodes: List<EpisodeInfo>
) {
    var nextLink: String? = null
    var first: EpisodeInfo? = null
}
