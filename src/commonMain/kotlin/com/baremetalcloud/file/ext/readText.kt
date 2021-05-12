package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend fun FileCommon.readText(): FileResult<String> = when(val my = readBytes()) {
    is FileResult.Failure -> FileResult.Failure(my.error)
    is FileResult.Success -> {
        lateinit var r: FileResult<String>
        withContext(Dispatchers.Default) {
            runCatching {
                r = FileResult.Success(my.value.decodeToString())
            }.onFailure {
                r = FileResult.Failure(it)
            }
        }
        r
    }
}