package com.islamux.kunuz.logic

import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.Treasure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareFormatterTest {

    private val treasure = Treasure(
        id = 7,
        title = "فضل الذكر",
        chapterId = ChapterId.DAILY_DHIKR,
        hadith = "أفضل الكلام بعد القرآن",
        narrator = "أبو هريرة",
        source = "متفق عليه",
        grade = "صحيح",
        explanation = "بيان فضل الذكر",
        action = "أكثر من الذكر في يومك",
        tags = listOf("ذكر")
    )

    private fun assertContains(output: String, field: String) {
        assertTrue("expected to contain «$field» but was: $output", output.contains(field))
    }

    @Test
    fun `includes the title`() = assertContains(formatTreasureForShare(treasure), "فضل الذكر")
    @Test
    fun `includes the hadith`() = assertContains(formatTreasureForShare(treasure), "أفضل الكلام بعد القرآن")
    @Test
    fun `includes the narrator`() = assertContains(formatTreasureForShare(treasure), "أبو هريرة")
    @Test
    fun `includes the source`() = assertContains(formatTreasureForShare(treasure), "متفق عليه")
    @Test
    fun `includes the grade`() = assertContains(formatTreasureForShare(treasure), "صحيح")
    @Test
    fun `includes the explanation`() = assertContains(formatTreasureForShare(treasure), "بيان فضل الذكر")
    @Test
    fun `includes the action`() = assertContains(formatTreasureForShare(treasure), "أكثر من الذكر في يومك")
    @Test
    fun `matches the web template byte for byte`() {
        val expected = """
            💎 كنز من السنة النبوية #7:
            ✨ فضل الذكر

            📜 نص الحديث الشريف:
            «أفضل الكلام بعد القرآن»

            👤 الراوي: أبو هريرة
            📚 المصدر: متفق عليه
            🏷️ الدرجة: صحيح

            💡 فضل الكنز وسره:
            بيان فضل الذكر

            🎯 كيف تعمل به؟
            أكثر من الذكر في يومك

            🤲 من كنوز السنة المطهرة للشيخ محمود المصري حفظه الله.
            📲 شاركها واكسب أجر من عمل بها (الدال على الخير كفاعله).
        """.trimIndent()

        assertEquals(expected, formatTreasureForShare(treasure))
    }
}