package org.shadow.studio.concatenate.backend.util

fun IntRange.toLongRange(): LongRange = LongRange(first.toLong(), last.toLong())
