package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.delete(): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            java.io.File(absolute).delete()
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}