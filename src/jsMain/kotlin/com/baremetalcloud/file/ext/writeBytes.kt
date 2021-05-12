package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*
import com.baremetalcloud.file.fs

public actual suspend fun FileCommon.writeBytes(array: ByteArray): FileResult<Unit> = fs.writeFileSync(absolute, array)