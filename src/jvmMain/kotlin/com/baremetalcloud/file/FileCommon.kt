package com.baremetalcloud.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

public actual class FileCommon actual constructor(public val absoluteFilename: String, public val context: CoroutineContext?) {
    public actual val name: String
        get() = absolute.substringAfterLast("/")
    public actual val absolute: String
        get() = absoluteFilename
    public actual val _context: CoroutineContext
        get() = context ?: defaultDispatcher

    override fun toString(): String = absoluteFilename

    public actual suspend fun mkdir(): FileResult<Boolean> = withContext(_context) {
        lateinit var r: FileResult<Boolean>
        runCatching {
            File(absoluteFilename).mkdir()
        }.onFailure {
            r = FileResult.Failure(it)
        }.onSuccess {
            r = FileResult.Success(it)
        }
        return@withContext r
    }

    public actual suspend fun deleteRecursively(): FileResult<Boolean> = withContext(_context) {
        lateinit var r: FileResult<Boolean>
        runCatching {
            File(absoluteFilename).deleteRecursively()
        }.onFailure { r = FileResult.Failure(it) }.onSuccess { r = FileResult.Success(it) }
        return@withContext r
    }

    public actual suspend fun listFiles(): FileResult<List<FileCommon>> = withContext(_context) {
        lateinit var r: FileResult<List<FileCommon>>
        runCatching {
            File(absolute).listFiles()!!.toList().map { FileCommon(it.absolutePath, context) }
        }.onFailure { r = FileResult.Failure(it) }.onSuccess { r = FileResult.Success(it) }
        return@withContext r
    }

    public actual suspend fun isDirectory(): FileResult<Boolean> = withContext(_context) {
        lateinit var r: FileResult<Boolean>
        runCatching {
            File(absolute).isDirectory
        }.onFailure { r = FileResult.Failure(it) }.onSuccess { r = FileResult.Success(it) }
        return@withContext r
    }

    public actual companion object {
        public actual val defaultDispatcher: CoroutineContext
            get() = Dispatchers.IO
    }

    public actual suspend fun use(block: suspend (FileCommon) -> Unit): Unit = withContext(_context) {
        block(this@FileCommon)
        delete()
    }

    public actual suspend fun delete(): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        withContext(Dispatchers.Default) {
            runCatching {
                java.io.File(absoluteFilename).delete()
                r = FileResult.Success(Unit)
            }.onFailure {
                r = FileResult.Failure(it)
            }
        }
        return@withContext r
    }

    public actual suspend fun exists(): FileResult<Boolean> = withContext(_context) {
        lateinit var r: FileResult<Boolean>
        withContext(Dispatchers.Default) {
            runCatching {
                r = FileResult.Success(java.io.File(absoluteFilename).exists())
            }.onFailure {
                r = FileResult.Failure<Boolean>(it)
            }
        }
        return@withContext r
    }

    public actual suspend fun readBytes(): FileResult<ByteArray> = withContext(_context) {
        lateinit var r: FileResult<ByteArray>
        runCatching {
            r = FileResult.Success(java.io.File(absoluteFilename).readBytes())
        }.onFailure {
            r = FileResult.Failure(it)
        }
        return@withContext r
    }

    public actual suspend fun renameTo(newName: String): FileResult<Boolean> = withContext(_context) {
        lateinit var result: FileResult<Boolean>
        runCatching {
            File(absoluteFilename).renameTo(File(newName))
            result = FileResult.Success(true)
        }.onFailure {
            result = FileResult.Failure<Boolean>(it)
        }
        return@withContext result
    }

    public actual suspend fun writeBytes(array: ByteArray): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        withContext(Dispatchers.Default) {
            runCatching {
                r = FileResult.Success(java.io.File(absoluteFilename).writeBytes(array))
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

    public actual suspend fun writeText(text: String): FileResult<Unit> = withContext(_context) {
        lateinit var r: FileResult<Unit>
        runCatching {
            r = FileResult.Success(writeBytes(text.encodeToByteArray()).getOrThrow())
        }.onFailure {
            r = FileResult.Failure(it)
        }
        return@withContext r
    }
}
