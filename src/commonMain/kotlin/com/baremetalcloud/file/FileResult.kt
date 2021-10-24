package com.baremetalcloud.file

public sealed class FileResult<T> {
    public data class Success<T>(val value: T) : FileResult<T>()
    public data class Failure<T>(val error: Throwable) : FileResult<T>()
}
public fun <T> FileResult<T>.throwing(): T = when (this) {
    is FileResult.Failure -> throw error
    is FileResult.Success -> value
}
public fun <T> FileResult<T>.getOrThrow(): T = when(this) {
    is FileResult.Failure -> throw error
    is FileResult.Success -> value
}

public fun <T> FileResult<T>.onFailure(action: (FileResult.Failure<T>) -> Unit): FileResult<T> {
    when (this) {
        is FileResult.Failure -> action(this)
        else -> {
        }
    }
    return this
}

public fun <T> FileResult<T>.onSuccess(action: (FileResult.Success<T>) -> Unit): FileResult<T> {
    when (this) {
        is FileResult.Success -> action(this)
        else -> {}
    }
    return this
}