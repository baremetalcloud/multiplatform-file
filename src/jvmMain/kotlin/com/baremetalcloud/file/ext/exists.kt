package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.exists(): FileResult<Boolean> {
    lateinit var r: FileResult<Boolean>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(java.io.File(absolute).exists())
        }.onFailure {
            r = FileResult.Failure<Boolean>(it)
        }
    }
    return r
}
