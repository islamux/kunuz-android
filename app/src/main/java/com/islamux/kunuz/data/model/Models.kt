package com.islamux.kunuz.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChapterId(val value: String) {
    @SerialName("daily-dhikr") DAILY_DHIKR("daily-dhikr"),
    @SerialName("prayers-mosques") PRAYERS_MOSQUES("prayers-mosques"),
    @SerialName("expiation-repentance") EXPIATION_REPENTANCE("expiation-repentance"),
    @SerialName("relief-ruqyah") RELIEF_RUQYAH("relief-ruqyah"),
    @SerialName("quran-virtues") QURAN_VIRTUES("quran-virtues"),
    @SerialName("morals-relations") MORALS_RELATIONS("morals-relations"),
    @SerialName("charity-ongoing") CHARITY_ONGOING("charity-ongoing"),
    @SerialName("fasting-seasons") FASTING_SEASONS("fasting-seasons"),
    @SerialName("manners-sunan") MANNERS_SUNAN("manners-sunan")
}

@Serializable
enum class ChapterIconName { Sun, Compass, Sparkles, ShieldCheck, BookOpen, HeartHandshake, Coins, Moon, ScrollText }

enum class TabId { ALL, CHAPTERS, FAVORITES, TASBEEH, CHECKLIST }

enum class FontSize(val label: String, val scale: Float) {
    NORMAL("متوسط", 1f),
    LARGE("كبير", 1.15f),
    XLARGE("كبير جداً", 1.3f)
}

@Serializable
data class Treasure(
    val id: Int,
    val title: String,
    val chapterId: ChapterId,
    val hadith: String,
    val narrator: String,
    val source: String,
    val grade: String,
    val explanation: String,
    val action: String,
    val repeatCount: Int? = null,
    val timeContext: String? = null,
    val tags: List<String> = emptyList(),
    val isSpecialDailyCandidate: Boolean? = null
)

@Serializable
data class Chapter(
    val id: ChapterId,
    val name: String,
    val shortName: String,
    val icon: ChapterIconName,
    val description: String,
    val colorClasses: String
)