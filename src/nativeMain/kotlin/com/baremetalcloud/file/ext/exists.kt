package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.*

public actual suspend fun FileCommon.exists(): FileResult<Boolean> {
    lateinit var r: FileResult<Boolean>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(access(absolute, F_OK) != -1)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}