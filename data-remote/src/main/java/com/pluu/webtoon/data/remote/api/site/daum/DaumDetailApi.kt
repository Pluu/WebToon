package com.pluu.webtoon.data.remote.api.site.daum

import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.model.DetailResult
import javax.inject.Inject

/**
 * 카카오 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
internal class DaumDetailApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : DetailApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailApi.Param): DetailResult {
        throw IllegalStateException("Un-support kakao webtoon")
    }
}
