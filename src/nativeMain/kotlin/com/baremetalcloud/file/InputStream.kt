package com.baremetalcloud.file

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import platform.posix.O_RDONLY
import platform.posix.SEEK_SET
import platform.posix.lseek
import platform.posix.open
import platform.posix.ssize_t
import platform.posix.stat

internal data class InputStream(val file: FileCommon) {

    private var fd = open(file.absoluteFilename, O_RDONLY)
    private var contentSize = nativeHeap.alloc<stat>().apply { stat(file.absoluteFilename, this.ptr) }.st_size - 1

    private var bytesRead = 0L

    fun readBytes(): ByteArray {
        val b = ByteArray(contentSize.toInt() + 1)
        readBytes(b)
        return b
    }

    private fun readBytes(bytes: ByteArray): ssize_t {
        return bytes.usePinned {
            platform.posix.read(fd, it.addressOf(0), bytes.size.convert())
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

    fun close() = platform.posix.close(fd)

    //val dbuff = if (contentSize-bytesRead > buffer.size) buffer.size.toInt() else (contentSize-bytesRead)
}