/*
 * Developed by Vitaly Nechaev
 * Copyright (c) 2019.  All rights reserved.
 *
 */

package kado.one.shafu.ezspannablestring

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextUtils.concat
import java.util.*

class SpannableStringCreator {
    private val parts = ArrayList<CharSequence>()
    private var length = 0
    private val spanMap: MutableMap<IntRange, Iterable<Any>> = HashMap()

    fun appendSpace(newText: CharSequence) = append(" ").append(newText)

    fun appendSpace(newText: CharSequence, spans: Iterable<Any>) = append(" ").append(newText, spans)

    fun appendLnNotBlank(newText: CharSequence, spans: Iterable<Any>) = applyIf({ !newText.isBlank() }) { appendLn(newText, spans) }

    fun appendLn(newText: CharSequence, spans: Iterable<Any>) = append("\n").append(newText, spans)

    fun append(newText: CharSequence, spans: Iterable<Any>) = apply {
        val end = newText.length
        parts.add(newText)
        spanMap[(length..length + end)] = spans
        length += end
    }

    fun append(newText: CharSequence) = apply {
        parts.add(newText)
        length += newText.length
    }

    inline fun applyIf(predicate: () -> Boolean, action: SpannableStringCreator.() -> SpannableStringCreator) = if (predicate()) action() else this

    fun toSpannableString() = SpannableString(concat(*parts.toTypedArray())).apply {
        spanMap.forEach {
            val range = it.key
            it.value.forEach {
                setSpan(it, range.start, range.endInclusive, SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}