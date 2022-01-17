package com.pluu.webtoon.weekly.model

import com.pluu.webtoon.domain.usecase.GetDefaultNaviUseCase
import com.pluu.webtoon.model.NAV_ITEM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor(
    defaultNaviUseCase: GetDefaultNaviUseCase
) {
    var navi: NAV_ITEM = defaultNaviUseCase()
}