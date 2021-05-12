package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import com.baremetalcloud.file.fs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.delete(): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            fs.unlinkSync(absolute)
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}