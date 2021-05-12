package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.writeBytes(array: ByteArray): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(java.io.File(absolute).writeBytes(array))
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}