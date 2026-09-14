package com.islamux.kunuz.speech

/**
 * Quote/bracket punctuation that TTS would read aloud awkwardly if spoken
 * verbatim (guillemets, parentheses, square brackets).
 *
 * Mirrors the web regex `text.replace(/«|»|[\(\)\[\]]/g, ' ')`. The web
 * collapses nothing; Android adds two sensible extensions:
 *   1. whitespace runs are collapsed to a single space, and
 *   2. leading/trailing whitespace is trimmed,
 * so short hadiths don't begin or end with an audible pause. Tashkeel is
 * intentionally kept — the web version speaks WITH tashkeel text.
 */
private val SPEECH_PUNCTUATION = Regex("""«|»|[()\[\]]""")
private val WHITESPACE_RUNS = Regex("""\s+""")

fun cleanForSpeech(text: String): String =
    text.replace(SPEECH_PUNCTUATION, " ").replace(WHITESPACE_RUNS, " ").trim()