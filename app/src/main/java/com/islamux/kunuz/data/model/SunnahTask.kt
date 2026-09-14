package com.islamux.kunuz.data.model

data class SunnahTask(
    val id: String,
    val title: String,
    val category: String,
    val reward: String
)

val DEFAULT_DAILY_TASKS: List<SunnahTask> = listOf(
    SunnahTask("morning-dhikr", "أذكار الصباح كاملة وسيد الاستغفار", "أذكار", "حفظ وكفاية ومغفرة تامة"),
    SunnahTask("evening-dhikr", "أذكار المساء والمعوذات ثلاثاً", "أذكار", "أمان من كل شر وهامة"),
    SunnahTask("salat-duha", "صلاة ركعتي الضحى", "صلاة", "صدقة عن ٣٦٠ مفصلاً في الجسد"),
    SunnahTask("ayat-kursi", "قراءة آية الكرسي دبر كل صلاة مكتوبة", "قرآن", "لا يمنعه من دخول الجنة إلا الموت"),
    SunnahTask("istighfar-100", "الاستغفار مائة مرة (أستغفر الله وأتوب إليه)", "استغفار", "طوبى وشجرة الجنة وتفريج الهم"),
    SunnahTask("salawat-nabi", "الصلاة على النبي ﷺ عشر مرات", "صلاة", "عشر صلوات ومحو عشر خطايا"),
    SunnahTask("daily-charity", "صدقة اليوم (ولو بشق تمرة أو ماء أو ابتسامة)", "صدقة", "تطفئ غضب الرب وتستظل بظلها"),
    SunnahTask("salat-witr", "صلاة الوتر قبل النوم", "صلاة", "إن الله وتر يحب الوتر")
)
