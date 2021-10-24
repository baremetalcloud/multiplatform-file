package com.baremetalcloud.file

import kotlinx.datetime.Clock

public fun FileCommon.Companion.tmpFile(prefix: String = "/tmp/tmpFile"): FileCommon {
    val CHARPOOL: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val randomAlphaNumeric = (1..8).map {
        kotlin.random.Random.nextInt(0, CHARPOOL.size)
    }.map(CHARPOOL::get).joinToString("")
    return FileCommon("${prefix}${Clock.System.now().toEpochMilliseconds()}${randomAlphaNumeric}", defaultDispatcher)
}



