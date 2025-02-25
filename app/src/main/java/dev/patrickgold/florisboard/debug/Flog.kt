/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalUnsignedTypes::class)

package dev.patrickgold.florisboard.debug

import android.content.Context
import android.os.Build
import android.util.Log
import java.lang.ref.WeakReference
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/** Type alias for a flog topic Integer. */
typealias FlogTopic = UInt

/** Type alias for a flog level Integer. */
typealias FlogLevel = UInt

/** Type alias for a flog output Integer. */
typealias FlogOutput = UInt

/**
 * Logs an error message returned by [block] either to the console or to a log file.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Flog.flogTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Flog.TOPIC_OTHER].
 * @param block The lambda expression which is called only if logging is enabled and
 *  the topics match. Must return a [String].
 */
inline fun flogError(topic: FlogTopic = Flog.TOPIC_OTHER, block: () -> String) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Flog.checkShouldFlog(topic, Flog.LEVEL_ERROR)) {
        Flog.log(Flog.LEVEL_ERROR, block())
    }
}

/**
 * Logs a warning message returned by [block] either to the console or to a log file.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Flog.flogTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Flog.TOPIC_OTHER].
 * @param block The lambda expression which is called only if logging is enabled and
 *  the topics match. Must return a [String].
 */
inline fun flogWarning(topic: FlogTopic = Flog.TOPIC_OTHER, block: () -> String) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Flog.checkShouldFlog(topic, Flog.LEVEL_WARNING)) {
        Flog.log(Flog.LEVEL_WARNING, block())
    }
}

/**
 * Logs an info message returned by [block] either to the console or to a log file.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Flog.flogTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Flog.TOPIC_OTHER].
 * @param block The lambda expression which is called only if logging is enabled and
 *  the topics match. Must return a [String].
 */
inline fun flogInfo(topic: FlogTopic = Flog.TOPIC_OTHER, block: () -> String) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Flog.checkShouldFlog(topic, Flog.LEVEL_INFO)) {
        Flog.log(Flog.LEVEL_INFO, block())
    }
}

/**
 * Logs a debug message returned by [block] either to the console or to a log file.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Flog.flogTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Flog.TOPIC_OTHER].
 * @param block The lambda expression which is called only if logging is enabled and
 *  the topics match. Must return a [String].
 */
inline fun flogDebug(topic: FlogTopic = Flog.TOPIC_OTHER, block: () -> String) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Flog.checkShouldFlog(topic, Flog.LEVEL_DEBUG)) {
        Flog.log(Flog.LEVEL_DEBUG, block())
    }
}

/**
 * Logs a wtf message returned by [block] either to the console or to a log file.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Flog.flogTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Flog.TOPIC_OTHER].
 * @param block The lambda expression which is called only if logging is enabled and
 *  the topics match. Must return a [String].
 */
inline fun flogWtf(topic: FlogTopic = Flog.TOPIC_OTHER, block: () -> String) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Flog.checkShouldFlog(topic, Flog.LEVEL_WTF)) {
        Flog.log(Flog.LEVEL_WTF, block())
    }
}

/**
 * Helper function to evaluate if a bit flag is set in an integer value.
 *
 * @param flag The flag to check if it is set.
 *
 * @return True if the flag is set, false otherwise.
 */
private infix fun UInt.isSet(flag: UInt): Boolean {
    return (this and flag) == flag
}

/**
 * Main helper object for FlorisBoard logging (=Flog). Manages the enabled
 * state and the active topics. Provides relevant helper functions for the
 * flog methods to properly work.
 *
 * This helper object uses some parts of the Timber library to assist in
 * logging. In particular:
 *  - [createStackElementTag] (converted to Kotlin, removed manual tagging).
 *  - [getTag] (converted to Kotlin but logic unmodified).
 *  - [log] (only the [OUTPUT_CONSOLE] part, converted to Kotlin).
 * Timber is licensed under the Apache 2.0 license, see the repo here:
 *  https://github.com/JakeWharton/timber
 */
@Suppress("MemberVisibilityCanBePrivate")
object Flog {
    const val TOPIC_NONE: FlogTopic =               UInt.MIN_VALUE
    const val TOPIC_OTHER: FlogTopic =              0x80000000u
    const val TOPIC_ALL: FlogTopic =                UInt.MAX_VALUE

    const val LEVEL_NONE: FlogLevel =               UInt.MIN_VALUE
    const val LEVEL_ERROR: FlogLevel =              0x01u
    const val LEVEL_WARNING: FlogLevel =            0x02u
    const val LEVEL_INFO: FlogLevel =               0x04u
    const val LEVEL_DEBUG: FlogLevel =              0x08u
    const val LEVEL_WTF: FlogLevel =                0x10u
    const val LEVEL_ALL: FlogLevel =                UInt.MAX_VALUE

    const val OUTPUT_CONSOLE: FlogOutput =          0x01u
    const val OUTPUT_FILE: FlogOutput =             0x02u

    /** The relevant call stack element is always on the 4th position, thus 4-1=3. */
    private const val CALL_STACK_INDEX: Int =       3

    /** The maximum log length limit. */
    private const val MAX_LOG_LENGTH: Int =         4000

    /** The maximum tag length limit. */
    private const val MAX_TAG_LENGTH: Int =         23

    /** Regex for matching anonymous classes. */
    private val ANONYMOUS_CLASS: Regex =            """(\\\$\\d+)+\$""".toRegex()

    private var applicationContext: WeakReference<Context> = WeakReference(null)
    private var isFloggingEnabled: Boolean = false
    private var flogTopics: FlogTopic = TOPIC_NONE
    private var flogLevels: FlogLevel = LEVEL_NONE
    private var flogOutputs: FlogOutput = OUTPUT_CONSOLE

    /**
     * Installs the flog utility for given [applicationContext] and sets the relevant
     * configuration variables based on the given config values.
     *
     * @param applicationContext The application context, used for file logging. The context
     *  will be wrapped in a [WeakReference] to prevent memory leaks.
     * @param isFloggingEnabled If logging is enabled. If this value is false, all calls to
     *  the flog methods will be ignored and no logs will be written, regardless of the topics
     *  and levels set.
     * @param flogTopics The enabled topics for this installation. Use [TOPIC_ALL] to enable
     *  all topics. If this value is [TOPIC_NONE], this essentially disables all logging.
     * @param flogLevels The enabled levels for this installation. Use [LEVEL_ALL] to enable
     *  all levels. If this value is [LEVEL_NONE], this essentially disables all logging.
     * @param flogOutputs The enabled outputs for this installation. Use either [OUTPUT_CONSOLE]
     *  for logging to Logcat or [OUTPUT_FILE] to a logging file.
     */
    fun install(
        applicationContext: Context,
        isFloggingEnabled: Boolean,
        flogTopics: FlogTopic,
        flogLevels: FlogLevel,
        flogOutputs: FlogOutput
    ) {
        this.applicationContext = WeakReference(applicationContext)
        this.isFloggingEnabled = isFloggingEnabled
        this.flogTopics = flogTopics
        this.flogLevels = flogLevels
        this.flogOutputs = flogOutputs
    }

    /**
     * Checks if a log message should be evaluated by checking [isFloggingEnabled] and
     * by matching the given [topic] and [level] values with the configured settings.
     *
     * @param topic The topic(s) to check for.
     * @param level The level(s) to check for.
     *
     * @return True if a log message should be evaluated, false otherwise.
     */
    fun checkShouldFlog(topic: FlogTopic, level: FlogLevel): Boolean {
        return isFloggingEnabled && (flogTopics isSet topic) && (flogLevels isSet level)
    }

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     */
    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = ANONYMOUS_CLASS.replace(element.className, "")
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    private fun getTag(): String {
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) {
            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX])
    }

    fun log(level: FlogLevel, msg: String) {
        when {
            flogOutputs isSet OUTPUT_CONSOLE -> {
                if (msg.length < MAX_LOG_LENGTH) {
                    androidLog(level, msg)
                } else {
                    // Split by line, then ensure each line can fit into Log's maximum length.
                    var i = 0
                    val length: Int = msg.length
                    while (i < length) {
                        var newline: Int = msg.indexOf('\n', i)
                        newline = if (newline != -1) newline else length
                        do {
                            val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                            val part: String = msg.substring(i, end)
                            androidLog(level, part)
                            i = end
                        } while (i < newline)
                        i++
                    }
                }
            }
            flogOutputs isSet OUTPUT_FILE -> {
                fileLog(level, msg)
            }
        }
    }

    private fun androidLog(level: FlogLevel, msg: String) {
        val tag = getTag()
        when {
            level isSet LEVEL_ERROR ->      Log.e(tag, msg)
            level isSet LEVEL_WARNING ->    Log.w(tag, msg)
            level isSet LEVEL_INFO ->       Log.i(tag, msg)
            level isSet LEVEL_DEBUG ->      Log.d(tag, msg)
            level isSet LEVEL_WTF ->        Log.wtf(tag, msg)
        }
    }

    private fun fileLog(level: FlogLevel, msg: String) {
        val context = applicationContext.get() ?: return
        // TODO: introduce file logging here for runtime debug logging
    }
}
