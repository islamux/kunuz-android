package com.islamux.kunuz.data

import com.islamux.kunuz.data.model.Treasure

object Tasbeeh {

    data class Event(val sound: Boolean, val haptic: Boolean, val confetti: Boolean)

    fun eventAfterIncrement(newCount: Int, target: Int, soundEnabled: Boolean): Event =
        Event(
            sound = soundEnabled,
            haptic = true,
            confetti = newCount >= target
        )

    fun defaultTarget(treasure: Treasure?): Int = treasure?.repeatCount ?: 33
}