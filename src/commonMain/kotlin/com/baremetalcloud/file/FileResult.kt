package com.baremetalcloud.file

public sealed class FileResult<T> {
    public data class Success<T>(val value: T): FileResult<T>()
    public data class Failure<T>(val error: Throwable): FileResult<T>()
}
public fun <T> FileResult<T>.getOrThrow(): T = when(this) {
    is FileResult.Failure -> throw error
    is FileResult.Success -> value
}