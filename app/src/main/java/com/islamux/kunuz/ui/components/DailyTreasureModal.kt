package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Share2
import com.composables.icons.lucide.Sparkles
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.logic.ArabicText
import com.islamux.kunuz.ui.theme.AmiriFontFamily
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark

@Composable
fun DailyTreasureModal(
    treasure: Treasure,
    chapter: Chapter?,
    isFavorite: Boolean,
    showTashkeel: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenShare: (Treasure) -> Unit,
    onOpenChecklist: () -> Unit,
    onClose: () -> Unit
) {
    val hadithText = if (showTashkeel) {
        treasure.hadith
    } else {
        ArabicText.stripTashkeelForDisplay(treasure.hadith)
    }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(16.dp)
                .testTag("daily-treasure-modal"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, KunuzBorder)
        ) {
            Column {
                DialogHeader(
                    colors = listOf(Color(0xFFD97706), Color(0xFFB45309)),
                    icon = Lucide.Flame,
                    title = "كنز اليوم النبوي المختار",
                    subtitle = "حديث وكنز خاص متجدد يرفع همتك في طاعة الله",
                    onClose = onClose
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    DailyTopRow(
                        treasure = treasure,
                        chapter = chapter,
                        isFavorite = isFavorite,
                        onToggleFavorite = onToggleFavorite,
                        onOpenShare = onOpenShare
                    )

                    Text(
                        text = treasure.title,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    DailyHadithBlock(
                        hadithText = hadithText,
                        narrator = treasure.narrator,
                        source = treasure.source,
                        grade = treasure.grade
                    )

                    DailyActionBlock(
                        title = "واجبك وتطبيقك العملي لهذا اليوم:",
                        text = treasure.action,
                        bg = Color(0xFFECFDF5),
                        border = Color(0xFFA7F3D0)
                    )

                    DailyActionBlock(
                        title = "شرح الكنز للشيخ محمود المصري:",
                        text = treasure.explanation,
                        bg = Color(0xFFFDFCF9),
                        border = Color(0xFFEBE6DC)
                    )
                }

                DialogFooter {
                    Surface(
                        modifier = Modifier
                            .clickable(onClick = onOpenChecklist)
                            .testTag("daily-checklist-cta"),
                        shape = RoundedCornerShape(12.dp),
                        color = KunuzPrimary
                    ) {
                        Text(
                            text = "أضف الكنز إلى جدول الورد اليومي",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyTopRow(
    treasure: Treasure,
    chapter: Chapter?,
    isFavorite: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenShare: (Treasure) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = Color(0xFFFEF3C7),
                contentColor = Color(0xFF92400E),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Text(
                    text = "الكنز #${treasure.id}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            chapter?.let { ch ->
                Surface(
                    color = KunuzPrimary.copy(alpha = 0.08f),
                    contentColor = KunuzPrimaryDark,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ChapterIcon(icon = ch.icon, modifier = Modifier.size(14.dp))
                        Text(
                            text = ch.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .testTag("daily-fav-btn")
                    .clickable { onToggleFavorite(treasure.id) }
                    .background(
                        color = if (isFavorite) Color(0xFFFEF2F2) else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Lucide.Bookmark,
                    contentDescription = if (isFavorite) "إزالة من المفضلة" else "حفظ في المفضلة",
                    tint = if (isFavorite) Color(0xFFDC2626) else Color(0xFF9CA3AF),
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onOpenShare(treasure) }
                    .testTag("daily-share-btn"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Lucide.Share2,
                    contentDescription = "مشاركة وبطاقة الكنز",
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyHadithBlock(
    hadithText: String,
    narrator: String,
    source: String,
    grade: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAF7F0), RoundedCornerShape(14.dp))
            .border(BorderStroke(2.dp, Color(0xFFE8DFCF)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "«$hadithText»",
            fontFamily = AmiriFontFamily,
            fontSize = 20.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildString {
                    append("الراوي: ")
                    append(narrator)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B5563)
            )
            Text(
                text = buildString {
                    append(source)
                    append(" (")
                    append(grade)
                    append(")")
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B5563)
            )
        }
    }
}

@Composable
private fun DailyActionBlock(title: String, text: String, bg: Color, border: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = BorderStroke(1.dp, border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Lucide.Sparkles,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xFF374151)
            )
        }
    }
}