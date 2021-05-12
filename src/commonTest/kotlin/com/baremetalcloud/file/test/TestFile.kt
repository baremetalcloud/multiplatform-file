package com.baremetalcloud.file.test


import com.baremetalcloud.file.*
import com.baremetalcloud.file.ext.*
import com.baremetalcloud.runblocking.runBlockingCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.datetime.Clock
import kotlin.test.*


class TestFile : CoroutineScope by GlobalScope {

    @Test
    fun shouldDetectCreatedFileExists() = runBlockingCommon {
        val f = FileCommon("/tmp/test.exists${Clock.System.now().toEpochMilliseconds()}")
        assertFalse(f.exists().getOrThrow())
        f.writeText("whatever")
        assertTrue(f.exists().getOrThrow())
    }

    @Test
    fun shouldReadStringWritten() = runBlockingCommon {
        val f = FileCommon("/tmp/test.readWriteText${Clock.System.now().toEpochMilliseconds()}")
        val content = "something to write"
        f.writeText(content)
        assertEquals(content, f.readText().getOrThrow())
    }

    @Test
    fun shouldReadBytesWritten() = runBlockingCommon {
        val f = FileCommon("/tmp/test.readWriteBytes${Clock.System.now().toEpochMilliseconds()}")
        val content = "anything"
        val wrote: ByteArray = content.encodeToByteArray()
        f.writeBytes(wrote)
        val read = f.readBytes()
        assertEquals(content, read.getOrThrow().decodeToString())
    }

    @Test
    fun shouldDeleteFile() = runBlockingCommon {
        val f = FileCommon("/tmp/test.delete${Clock.System.now().toEpochMilliseconds()}")
        f.writeText("eeeee")
        assertTrue(f.exists().getOrThrow())
        f.delete()
        assertFalse(f.exists().getOrThrow())
    }

}
