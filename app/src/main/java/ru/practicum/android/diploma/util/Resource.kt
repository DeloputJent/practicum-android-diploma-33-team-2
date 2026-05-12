package ru.practicum.android.diploma.util

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(val kind: ErrorKind = ErrorKind.SERVER, data: T? = null) : Resource<T>(data)
    object Loading : Resource<Nothing>()
}

enum class ErrorKind {
    NO_INTERNET,
    SERVER,
}
