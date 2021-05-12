package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import com.baremetalcloud.file.fs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun FileCommon.readBytes(): FileResult<ByteArray> {
    lateinit var r: FileResult<ByteArray>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(fs.readFileSync(absolute, "utf8").toString().encodeToByteArray())
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}