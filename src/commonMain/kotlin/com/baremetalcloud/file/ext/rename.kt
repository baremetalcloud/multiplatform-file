package com.baremetalcloud.file.ext

import com.baremetalcloud.file.*

public expect fun FileCommon.renameTo(newName: String): FileResult<Boolean>