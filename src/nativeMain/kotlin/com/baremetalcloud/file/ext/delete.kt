package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.unlink

public actual suspend fun FileCommon.delete(): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            unlink(absolute)
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}