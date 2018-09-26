package com.pluu.webtoon.di

import com.pluu.webtoon.db.RealmHelper
import org.koin.dsl.module.module

val dbModule = module {
    single { RealmHelper() }
}
