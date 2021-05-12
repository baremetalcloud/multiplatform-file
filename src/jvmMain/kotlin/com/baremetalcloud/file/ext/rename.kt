package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import java.io.File

public actual fun FileCommon.renameTo(newName: String): FileResult<Boolean> {
    lateinit var result: FileResult<Boolean>
    runCatching {
        File(absolute).renameTo(File(newName))
        result = FileResult.Success(true)
    }.onFailure {
        result = FileResult.Failure<Boolean>(it)
    }
    return result
}