package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult
import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.*

public actual suspend fun FileCommon.writeBytes(array: ByteArray): FileResult<Unit> {
    lateinit var r: FileResult<Unit>
    withContext(Dispatchers.Default) {
        runCatching {
            OutputStream(this@writeBytes).write(array)
            r = FileResult.Success(Unit)
        }.onFailure {
            r = FileResult.Failure(it)
        }
    }
    return r

}

private data class OutputStream(val file: FileCommon) {
    private var fd: Int = open(file.absolute, O_RDWR or O_CREAT, 438)
    private var contentSize = nativeHeap.alloc<stat>().apply { stat(file.absolute, this.ptr) }.st_size -1

    private fun writeBytes(bytes: ByteArray, offset: Long = 0L): ssize_t {
        if (offset>0) skip(offset)
        return bytes.usePinned {
            write(fd, it.addressOf(0), bytes.size.convert())
        }
    }

    fun write(bytes: ByteArray) = writeBytes(bytes)

    fun write(byte: Byte) = writeBytes(byteArrayOf(byte))

    fun skip(n: Long): Long = lseek(fd, n.convert(), SEEK_SET)

    fun close() = close(fd)

    fun getFD() = fd
}
