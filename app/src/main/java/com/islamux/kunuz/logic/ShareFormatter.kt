package com.islamux.kunuz.logic

import com.islamux.kunuz.data.model.Treasure

fun formatTreasureForShare(treasure: Treasure): String {
    return """
        💎 كنز من السنة النبوية #${treasure.id}:
        ✨ ${treasure.title}

        📜 نص الحديث الشريف:
        «${treasure.hadith}»

        👤 الراوي: ${treasure.narrator}
        📚 المصدر: ${treasure.source}
        🏷️ الدرجة: ${treasure.grade}

        💡 فضل الكنز وسره:
        ${treasure.explanation}

        🎯 كيف تعمل به؟
        ${treasure.action}

        🤲 من كنوز السنة المطهرة للشيخ محمود المصري حفظه الله.
        📲 شاركها واكسب أجر من عمل بها (الدال على الخير كفاعله).
    """.trimIndent()
}