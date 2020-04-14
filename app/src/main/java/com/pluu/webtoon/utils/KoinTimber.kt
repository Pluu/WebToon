package com.pluu.webtoon.utils

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class KoinTimber(
    level: Level = Level.INFO
) : Logger(level) {

    private val TAG = "LOG"

    override fun log(level: Level, msg: MESSAGE) {
        if (this.level <= level) {
            logOnLevel(msg, level)
        }
    }

    private fun logOnLevel(msg: MESSAGE, level: Level) {
        when (level) {
            Level.DEBUG -> Timber.tag(TAG).d(msg)
            Level.INFO -> Timber.tag(TAG).i(msg)
            Level.ERROR -> Timber.tag(TAG).e(msg)
            else -> Timber.tag(TAG).e(msg)
        }
    }
}