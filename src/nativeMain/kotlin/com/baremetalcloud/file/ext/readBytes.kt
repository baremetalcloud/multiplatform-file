package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.*

public actual suspend fun FileCommon.readBytes(): FileResult<ByteArray> {
    lateinit var r: FileResult<ByteArray>
    withContext(Dispatchers.Default) {
        runCatching {
            r = FileResult.Success(InputStream(this@readBytes).readBytes())
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r
}

private data class InputStream(val file: FileCommon) {

    private var fd = open(file.absolute, O_RDONLY)
    private var contentSize = nativeHeap.alloc<stat>().apply { stat(file.absolute, this.ptr) }.st_size - 1

    private var bytesRead = 0L

    fun readBytes(): ByteArray {
        val b = ByteArray(contentSize.toInt() + 1)
        readBytes(b)
        return b
    }

    private fun readBytes(bytes: ByteArray): ssize_t {
        return bytes.usePinned {
            read(fd, it.addressOf(0), bytes.size.convert())
        }
    }

    fun read(buffer: ByteArray): ByteArray {
        bytesRead += readBytes(buffer)
        //if (ret == 0L) break; /* EOF */
        //if (ret == -1L) { break; /* Handle error */ }
        return buffer
    }

    fun read(chunk: Int): ByteArray {
        val dbuff = if (contentSize - bytesRead > chunk) chunk else (contentSize - bytesRead).convert()
        return read(ByteArray(dbuff))
    }

    fun read() = read(1)[0]

    fun skip(n: Long): Long = lseek(fd, n.convert(), SEEK_SET)

    fun available() = bytesRead < contentSize

    fun getFD() = fd

    fun close() = close(fd)

    //val dbuff = if (contentSize-bytesRead > buffer.size) buffer.size.toInt() else (contentSize-bytesRead)
}