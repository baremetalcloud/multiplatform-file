package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.readBytes(): FileResult<ByteArray> {
    lateinit var r: FileResult<ByteArray>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(java.io.File(absolute).readBytes())
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}
