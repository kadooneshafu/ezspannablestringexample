/*
 * Developed by Vitaly Nechaev
 * Copyright (c) 2019.  All rights reserved.
 *
 */

package kado.one.shafu.ezspannablestring

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringRes
import java.util.*

inline fun Context.resSpans(options: ResSpans.() -> Unit) =
    ResSpans(this).apply(options)

fun Resources.getText(@StringRes id: Int, vararg formatArgs: Any?) =
    getSpannable(id, *formatArgs.filterNotNull().map { it to emptyList<Any>() }.toTypedArray())

fun Resources.getSpannable(@StringRes id: Int, vararg spanParts: Pair<Any, Iterable<Any>>): CharSequence {
    val resultCreator = SpannableStringCreator()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Formatter(
            SpannableAppendable(resultCreator, *spanParts),
            configuration.locales.get(0)
        ).format(getString(id), *spanParts.map { it.first }.toTypedArray())
    } else {
        Formatter(
            SpannableAppendable(resultCreator, *spanParts),
            configuration.locale
        ).format(getString(id), *spanParts.map { it.first }.toTypedArray())
    }
    return resultCreator.toSpannableString()
}
