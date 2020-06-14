package com.ninovanhooff.negenwnegenw.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Only invoke [destinationFunction] when it is not invoked another time within [waitMs]
 * As long as the coroutine is not cancelled, the last invocation of [destinationFunction]
 * will be executed.
 *
 * Visual explanation on [RxMarbles](https://rxmarbles.com/#debounce) */
fun <T> debounce(
    waitMs: Long,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}