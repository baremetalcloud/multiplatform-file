package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend fun FileCommon.replace(search: String, replace: String): FileResult<Unit>  {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            writeText(readText().getOrThrow().replace(search, replace))
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}
