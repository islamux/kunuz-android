package com.islamux.kunuz.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SunnahTaskTest {

    private val webRows = listOf(
        SunnahTask("morning-dhikr", "أذكار الصباح كاملة وسيد الاستغفار", "أذكار", "حفظ وكفاية ومغفرة تامة"),
        SunnahTask("evening-dhikr", "أذكار المساء والمعوذات ثلاثاً", "أذكار", "أمان من كل شر وهامة"),
        SunnahTask("salat-duha", "صلاة ركعتي الضحى", "صلاة", "صدقة عن ٣٦٠ مفصلاً في الجسد"),
        SunnahTask("ayat-kursi", "قراءة آية الكرسي دبر كل صلاة مكتوبة", "قرآن", "لا يمنعه من دخول الجنة إلا الموت"),
        SunnahTask("istighfar-100", "الاستغفار مائة مرة (أستغفر الله وأتوب إليه)", "استغفار", "طوبى وشجرة الجنة وتفريج الهم"),
        SunnahTask("salawat-nabi", "الصلاة على النبي ﷺ عشر مرات", "صلاة", "عشر صلوات ومحو عشر خطايا"),
        SunnahTask("daily-charity", "صدقة اليوم (ولو بشق تمرة أو ماء أو ابتسامة)", "صدقة", "تطفئ غضب الرب وتستظل بظلها"),
        SunnahTask("salat-witr", "صلاة الوتر قبل النوم", "صلاة", "إن الله وتر يحب الوتر")
    )

    @Test
    fun `default task ids match the web list exactly in order`() {
        assertEquals(
            listOf(
                "morning-dhikr",
                "evening-dhikr",
                "salat-duha",
                "ayat-kursi",
                "istighfar-100",
                "salawat-nabi",
                "daily-charity",
                "salat-witr"
            ),
            DEFAULT_DAILY_TASKS.map { it.id }
        )
    }

    @Test
    fun `default tasks match the web table byte for byte`() {
        assertEquals(webRows, DEFAULT_DAILY_TASKS)
    }
}
