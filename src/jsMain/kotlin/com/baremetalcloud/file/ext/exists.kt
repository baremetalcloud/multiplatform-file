package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import com.baremetalcloud.file.fs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.exists(): FileResult<Boolean> {
    lateinit var r: FileResult<Boolean>
    withContext(Dispatchers.Default) {
        runCatching {

            r = FileResult.Success(fs.existsSync(absolute) as Boolean)
        }.onFailure {
            r = FileResult.Failure<Boolean>(it)
        }
    }
    return r
}