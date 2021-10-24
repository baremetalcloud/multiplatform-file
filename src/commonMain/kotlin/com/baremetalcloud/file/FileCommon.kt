package com.baremetalcloud.file

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

import kotlinx.datetime.Clock

public expect class FileCommon(absoluteFilename: String, context: CoroutineContext? = null) {
    public val _context: CoroutineContext
    public val name: String
    public val absolute: String
    public suspend fun mkdir(): FileResult<Boolean>
    public suspend fun deleteRecursively(): FileResult<Boolean>
    public suspend fun listFiles(): FileResult<List<FileCommon>>
    public suspend fun isDirectory(): FileResult<Boolean>
    public suspend fun delete(): FileResult<Unit>
    public suspend fun exists(): FileResult<Boolean>
    public suspend fun readBytes(): FileResult<ByteArray>
    public suspend fun readText(): FileResult<String>
    public suspend fun writeBytes(array: ByteArray): FileResult<Unit>
    public suspend fun writeText(text: String): FileResult<Unit>
    public suspend fun renameTo(newName: String): FileResult<Boolean>
    public suspend fun replace(search: String, replace: String): FileResult<Unit>

    public suspend fun use(block: suspend (FileCommon) -> Unit)

    public companion object {
        public val defaultDispatcher: CoroutineContext
    }

}

