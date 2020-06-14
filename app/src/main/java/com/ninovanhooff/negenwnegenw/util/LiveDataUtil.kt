package com.ninovanhooff.negenwnegenw.util

object LiveDataUtil {

    /** Can only be consumed once. Will not be re-emitted when the Fragment attaches a second time
     * to ViewModel, for example
     */
    open class Event<out T>(private val content: T) {

        var hasBeenHandled = false
            private set // Allow external read but not write

        /**
         * Returns the content and prevents its use again.
         */
        fun getContentIfNotHandledOrReturnNull(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

        /**
         * Returns the content, even if it's already been handled.
         */
        fun peekContent(): T = content
    }
}