package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend fun FileCommon.writeText(text: String): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(writeBytes(text.encodeToByteArray()).getOrThrow())
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r

}