package com.baremetalcloud.file.ext

import com.baremetalcloud.file.FileCommon

private fun FileCommon.toJavaFile(): java.io.File = java.io.File(absoluteFilename)
