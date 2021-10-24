//package com.baremetalcloud.file
//
//import com.baremetalcloud.runblocking.runBlockingCommon
//import kotlinx.datetime.Clock
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertFalse
//import kotlin.test.assertTrue
//
//
//class TestFile  {
//
//    @Test
//    fun shouldDetectCreatedFileExists() = runBlockingCommon {
//        val f = FileCommon("/tmp/test.exists${Clock.System.now().toEpochMilliseconds()}")
//        assertFalse(f.exists().getOrThrow())
//        f.writeText("whatever")
//        assertTrue(f.exists().getOrThrow())
//    }
//
//    @Test
//    fun shouldReadStringWritten() = runBlockingCommon {
//        val f = FileCommon("/tmp/test.readWriteText${Clock.System.now().toEpochMilliseconds()}")
//        val content = "something to write"
//        f.writeText(content)
//        assertEquals(content, f.readText().getOrThrow())
//    }
//
//    @Test
//    fun shouldReadBytesWritten() = runBlockingCommon {
//        val f = FileCommon("/tmp/test.readWriteBytes${Clock.System.now().toEpochMilliseconds()}")
//        val content = "anything"
//        val wrote: ByteArray = content.encodeToByteArray()
//        f.writeBytes(wrote)
//        val read = f.readBytes()
//        assertEquals(content, read.getOrThrow().decodeToString())
//    }
//
//    @Test
//    fun shouldDeleteFile() = runBlockingCommon {
//        val f = FileCommon("/tmp/test.delete${Clock.System.now().toEpochMilliseconds()}")
//        f.writeText("eeeee")
//        assertTrue(f.exists().getOrThrow())
//        f.delete()
//        assertFalse(f.exists().getOrThrow())
//    }
//
//}
