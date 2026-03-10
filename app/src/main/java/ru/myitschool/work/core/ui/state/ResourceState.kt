package ru.myitschool.work.core.ui.state

sealed class ResourceState<out T> {
    object Idle : ResourceState<Nothing>()

    data class Loading<T>(val cached: T? = null) : ResourceState<T>()
    data class Refreshing<T>(val cached: T? = null) : ResourceState<T>()

    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val errorMessage: String, val throwable: Throwable? = null, val cached: T? = null) : ResourceState<T>()
}

val ResourceState<*>.hasData: Boolean
    get() = when (this) {
        is ResourceState.Success -> true
        is ResourceState.Loading -> cached != null
        is ResourceState.Refreshing -> cached != null
        is ResourceState.Error -> cached != null
        ResourceState.Idle -> false
    }

val ResourceState<*>.hasNoData: Boolean
    get() = !hasData

fun <T> ResourceState<T>.dataOrNull(): T? = when (this) {
    is ResourceState.Success -> this.data
    is ResourceState.Loading -> this.cached
    is ResourceState.Refreshing -> this.cached
    is ResourceState.Error -> this.cached
    ResourceState.Idle -> null
}

fun <T> ResourceState<T>.toIdle(): ResourceState.Idle =  // TODO: Should Idle store cached?
    ResourceState.Idle

fun <T> ResourceState<T>.toRefreshing(): ResourceState.Refreshing<T> =
    ResourceState.Refreshing(cached = this.dataOrNull())

fun <T> ResourceState<T>.toLoading(): ResourceState.Loading<T> =
    ResourceState.Loading(cached = this.dataOrNull())

fun <T> ResourceState<T>.toError(errorMessage: String, throwable: Throwable? = null): ResourceState.Error<T> =
    ResourceState.Error(errorMessage = errorMessage, throwable = throwable, cached = this.dataOrNull())

val ResourceState<*>.isIdle: Boolean
    get() = this is ResourceState.Idle

val ResourceState<*>.isLoading: Boolean
    get() = this is ResourceState.Loading

val ResourceState<*>.isRefreshing: Boolean
    get() = this is ResourceState.Refreshing

val ResourceState<*>.isFetching: Boolean
    get() = this.isLoading || this.isRefreshing

val ResourceState<*>.isSuccess: Boolean
    get() = this is ResourceState.Success

val ResourceState<*>.isError: Boolean
    get() = this is ResourceState.Error

inline fun ResourceState<*>.whenIdle(block: () -> Unit) {
    if (this is ResourceState.Idle) block()
}

inline fun ResourceState<*>.whenLoading(block: () -> Unit) {
    if (this is ResourceState.Loading) block()
}

inline fun ResourceState<*>.whenRefreshing(block: () -> Unit) {
    if (this is ResourceState.Refreshing) block()
}

inline fun ResourceState<*>.whenFetching(block: () -> Unit) {
    if (this is ResourceState.Loading || this is ResourceState.Refreshing) block()
}

inline fun <T> ResourceState<T>.whenSuccess(block: (T) -> Unit) {
    if (this is ResourceState.Success) block(data)
}

inline fun <T> ResourceState<T>.whenError(block: (errorMessage: String, error: Throwable?, cached: T?) -> Unit) {
    if (this is ResourceState.Error) block(errorMessage, throwable, cached)
}

inline fun <T> ResourceState<T>.whenData(block: (T) -> Unit) {
    dataOrNull()?.let(block)
}

inline fun <T> ResourceState<T>.whenState(
    vararg states: kotlin.reflect.KClass<out ResourceState<*>>,
    containsData: Boolean? = null,
    block: (ResourceState<T>) -> Unit
) {
    val matchesState = states.any { it.isInstance(this) }

    val matchesData = when (containsData) {
        true -> hasData
        false -> hasNoData
        null -> true
    }

    if (matchesState && matchesData) block(this)
}

inline fun <T> ResourceState<T>.whenState(
    condition: ResourceState<T>.() -> Boolean,
    block: (ResourceState<T>) -> Unit
) {
    if (condition()) block(this)
}
