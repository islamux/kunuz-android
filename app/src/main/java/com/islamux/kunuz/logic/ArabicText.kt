package com.islamux.kunuz.logic

object ArabicText {

    fun removeTashkeel(text: String): String {
        if (text.isEmpty()) return ""
        return text
            .replace("\u0640", "")                                                       // tatweel
            .replace(Regex("[\u064B-\u065F\u0670]"), "")                                 // harakat
            .replace(Regex("[إأآٱ]"), "ا")                                                // alef variants
            .replace("ة", "ه")                                                           // teh marbuta
            .replace("ى", "ي")                                                           // alef maqsura
    }

    fun matchesSearch(content: String, query: String): Boolean {
        if (query.trim().isEmpty()) return true
        val cleanContent = removeTashkeel(content.lowercase())
        val cleanQuery = removeTashkeel(query.lowercase().trim())
        return cleanContent.contains(cleanQuery)
    }

    fun stripTashkeelForDisplay(text: String): String {
        if (text.isEmpty()) return ""
        return text.replace(Regex("[\u064B-\u0652\u0670]"), "")
    }

    private val ARABIC_DIGITS = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

    fun toArabicDigits(n: Int): String {
        return n.toString().map { digit -> ARABIC_DIGITS[digit.digitToInt()] }.joinToString("")
    }
}