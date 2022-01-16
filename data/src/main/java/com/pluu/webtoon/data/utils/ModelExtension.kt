package com.pluu.webtoon.utils.com.pluu.webtoon.data.utils

import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.Toon

internal fun Toon.toDBModel(): DBToon = DBToon(service, toonId)

internal fun Episode.toDBModel(): DBEpisode = DBEpisode(service, toonId, episodeId)

internal fun DBEpisode.toModel(): Episode = Episode(service, toonId, episodeId)