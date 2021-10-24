package com.baremetalcloud.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.F_OK
import platform.posix.access
import platform.posix.unlink
import kotlin.coroutines.CoroutineContext

public actual class FileCommon actual constructor(public val absoluteFilename: String, private val context: CoroutineContext?) {
    public actual val name: String
        get() = absolute.substringAfterLast("/")
    public actual val absolute: String
        get() = absoluteFilename
    public actual val _context: CoroutineContext
        get() = context ?: defaultDispatcher


    public actual suspend fun mkdir(): FileResult<Boolean> = withContext(_context) {
        TODO("Not yet implemented")
    }

    public actual suspend fun deleteRecursively(): FileResult<Boolean> = withContext(_context) {
        TODO("Not yet implemented")
    }

    public actual suspend fun listFiles(): FileResult<List<FileCommon>> = withContext(_context) {
        TODO("Not yet implemented")
    }

    public actual suspend fun isDirectory(): FileResult<Boolean> = withContext(_context) {
        TODO("Not yet implemented")
    }


    public actual companion object {
        public actual val defaultDispatcher: CoroutineContext
            get() = Dispatchers.Default

    }

    public actual suspend fun use(block: suspend (FileCommon) -> Unit): Unit = withContext(_context) {
        block(this@FileCommon)
        delete().throwing()
    }

    public actual suspend fun delete(): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        withContext(Dispatchers.Default) {
            runCatching {
                unlink(absoluteFilename)
                r = FileResult.Success(Unit)
            }.onFailure {
                r = FileResult.Failure(it)
            }
        }
        return@withContext r
    }

    public actual suspend fun exists(): FileResult<Boolean> = withContext(_context) {
        lateinit var r: FileResult<Boolean>
        runCatching {
            r = FileResult.Success(access(absoluteFilename, F_OK) != -1)
        }.onFailure {
            r = FileResult.Failure(it)
        }
        return@withContext r
    }

    public actual suspend fun readBytes(): FileResult<ByteArray> = withContext(_context) {
        lateinit var r: FileResult<ByteArray>
        runCatching {
            r = FileResult.Success(InputStream(this@FileCommon).readBytes())
        }.onFailure {
            r = FileResult.Failure(it)
        }

        return@withContext r
    }

    public actual suspend fun renameTo(newName: String): FileResult<Boolean> = withContext(_context) {
        TODO("Not yet implemented")
    }

    public actual suspend fun writeBytes(array: ByteArray): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        withContext(Dispatchers.Default) {
            runCatching {
                OutputStream(this@FileCommon).write(array)
                r = FileResult.Success(Unit)
            }.onFailure {
                r = FileResult.Failure(it)
            }
        }
        return@withContext r
    }

    public actual suspend fun readText(): FileResult<String> = withContext(_context) {
        when (val my = readBytes()) {
            is FileResult.Failure -> FileResult.Failure(my.error)
            is FileResult.Success -> {
                lateinit var r: FileResult<String>
                runCatching {
                    r = FileResult.Success(my.value.decodeToString())
                }.onFailure {
                    r = FileResult.Failure(it)
                }
                r
            }
        }
    }
    public actual suspend fun writeText(text: String): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        runCatching {
            r = FileResult.Success(writeBytes(text.encodeToByteArray()).getOrThrow())
        }.onFailure {
            r = FileResult.Failure(it)
        }
        return@withContext r
    }
    public actual suspend fun replace(search: String, replace: String): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        runCatching {
            writeText(readText().getOrThrow().replace(search, replace))
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
        return@withContext r
    }

}
