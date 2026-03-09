package ru.myitschool.work.core.ui.state

sealed class ResourceState<out T> {
    object Idle : ResourceState<Nothing>()
    object Loading : ResourceState<Nothing>()
    object Refreshing : ResourceState<Nothing>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error(val errorMessage: String, val throwable: Throwable?) : ResourceState<Nothing>()
}
