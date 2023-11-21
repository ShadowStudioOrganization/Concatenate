package org.shadow.studio.concatenate.backend.util

import kotlinx.datetime.Instant

inline fun parseOffsetIsoTime(iso: String) = Instant.parse(iso)