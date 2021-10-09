package com.pluu.webtoon.support.daum

import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.model.DetailResult

/**
 * 카카오 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
internal class DaumDetailApi(
    private val networkUseCase: INetworkUseCase
) : DetailUseCase, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailRequest): DetailResult {
        throw IllegalStateException("Un-support kakao webtoon")
    }
}
