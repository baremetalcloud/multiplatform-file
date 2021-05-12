package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon
import com.baremetalcloud.file.FileResult

public expect suspend fun FileCommon.exists(): FileResult<Boolean>

