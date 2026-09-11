package com.islamux.kunuz.logic

import java.time.LocalDate

fun localDateKey(d: LocalDate): String {
    return "%04d-%02d-%02d".format(d.year, d.monthValue, d.dayOfMonth)
}
