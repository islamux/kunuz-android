package com.islamux.kunuz.logic

fun nextStreak(
    lastKey: String?,
    currentStreak: Int,
    todayKey: String,
    yesterdayKey: String
): Pair<Int, String>? {
    if (lastKey == todayKey) return null
    val next = if (lastKey == yesterdayKey) currentStreak + 1 else 1
    return next to todayKey
}

fun effectiveStreak(
    lastKey: String?,
    storedStreak: Int,
    todayKey: String,
    yesterdayKey: String
): Int {
    if (lastKey == null) return 0
    if (lastKey == todayKey || lastKey == yesterdayKey) return storedStreak
    return 0
}

fun countCompleted(tasks: Map<String, Boolean>): Int = tasks.values.count { it }
