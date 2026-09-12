package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.Copy
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Share2
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Tag
import com.composables.icons.lucide.Volume2
import com.composables.icons.lucide.VolumeX
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.logic.ArabicText
import com.islamux.kunuz.logic.formatTreasureForShare
import com.islamux.kunuz.ui.theme.AmiriFontFamily
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark
import com.islamux.kunuz.ui.theme.LocalKunuzTextScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val KunuzCardSurface = Color(0xFFFFFFFF)
private val KunuzHeadline = Color(0xFF1F2937)
private val KunuzBody = Color(0xFF4B5563)
private val KunuzMuted = Color(0xFF6B7280)
private val KunuzLightGray = Color(0xFF9CA3AF)
private val KunuzDangerSoftBg = Color(0xFFFEF2F2)
private val KunuzAmberChipBg = Color(0xFFFEF3C7)
private val KunuzAmberChipText = Color(0xFF92400E)
private val KunuzAmberChipBorder = Color(0xFFFDE68A)
private val KunuzHadithFrame = Color(0xFFFAF7F0)
private val KunuzFrameBorder = Color(0xFFEBE3D3)
private val KunuzInk = Color(0xFF111827)
private val KunuzSoftGreen = Color(0xFFECFDF5)
private val KunuzGreenBorder = Color(0xFFA7F3D0)
private val KunuzGreenStrong = Color(0xFF059669)
private val KunuzGreenDark = Color(0xFF065F46)
private val KunuzFooterBg = Color(0xFFFBF9F5)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreasureCard(
    treasure: Treasure,
    chapter: Chapter? = null,
    isFavorite: Boolean,
    onToggleFavorite: (Int) -> Unit,
    fontSize: FontSize,
    showTashkeel: Boolean,
    onOpenShare: (Treasure) -> Unit,
    onOpenTasbeeh: (Treasure) -> Unit,
    onSelectTag: (String) -> Unit = {},
    onToggleListen: () -> Unit = {},
    isListening: Boolean = false,
    textScale: Float = LocalKunuzTextScale.current,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }
    var counter by remember { mutableStateOf(0) }

    val hadithText = if (showTashkeel) treasure.hadith
    else ArabicText.stripTashkeelForDisplay(treasure.hadith)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("TreasureCard"),
        shape = RoundedCornerShape(16.dp),
        color = KunuzCardSurface,
        border = BorderStroke(1.dp, KunuzBorder)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                CardHeader(
                    treasure = treasure,
                    chapter = chapter,
                    isFavorite = isFavorite,
                    onToggleFavorite = onToggleFavorite,
                    onOpenShare = onOpenShare
                )

                Text(
                    text = treasure.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = KunuzHeadline,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                )

                HadithFrame(
                    hadithText = hadithText,
                    hadithFontSize = 20.sp * fontSize.scale * textScale,
                    treasure = treasure
                )

                if (treasure.repeatCount != null && treasure.repeatCount > 1) {
                    RepeatCounterBlock(
                        treasure = treasure,
                        repeatCount = treasure.repeatCount ?: 0,
                        counter = counter,
                        onIncrement = { counter += 1 },
                        onOpenTasbeeh = onOpenTasbeeh
                    )
                }

                ActionBlock(treasure = treasure)

                ExplanationBlock(
                    expanded = expanded,
                    treasure = treasure,
                    onToggle = { expanded = !expanded }
                )

                if (treasure.tags.isNotEmpty()) {
                    TagsRow(tags = treasure.tags, onSelectTag = onSelectTag)
                }
            }

            CardFooter(
                treasure = treasure,
                isListening = isListening,
                onToggleListen = onToggleListen
            )
        }
    }
}

@Composable
private fun CardHeader(
    treasure: Treasure,
    chapter: Chapter?,
    isFavorite: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenShare: (Treasure) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                color = KunuzPrimary.copy(alpha = 0.10f),
                contentColor = KunuzPrimary,
                shape = RoundedCornerShape(8.dp)
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
                            text = ch.shortName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            treasure.timeContext?.let { time ->
                Surface(
                    color = KunuzAmberChipBg,
                    contentColor = KunuzAmberChipText,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, KunuzAmberChipBorder)
                ) {
                    Text(
                        text = "⏱️ $time",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .testTag("fav-btn-${treasure.id}")
                    .clickable { onToggleFavorite(treasure.id) }
                    .background(
                        color = if (isFavorite) KunuzDangerSoftBg else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
            ) {
                Icon(
                    imageVector = Lucide.Bookmark,
                    contentDescription = if (isFavorite) "إزالة من المفضلة" else "حفظ في المفضلة",
                    tint = if (isFavorite) Color(0xFFDC2626) else KunuzLightGray,
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onOpenShare(treasure) }
                    .padding(7.dp)
            ) {
                Icon(
                    imageVector = Lucide.Share2,
                    contentDescription = "مشاركة وبطاقة الكنز",
                    tint = KunuzMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun HadithFrame(
    hadithText: String,
    hadithFontSize: androidx.compose.ui.unit.TextUnit,
    treasure: Treasure
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = KunuzHadithFrame,
        border = BorderStroke(1.dp, KunuzFrameBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "«$hadithText»",
                fontSize = hadithFontSize,
                lineHeight = hadithFontSize * 1.6f,
                fontFamily = AmiriFontFamily,
                color = KunuzInk,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CitationLabel(label = "الراوي:", value = treasure.narrator)
                CitationLabel(label = "المصدر:", value = treasure.source)
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
            ) {
                Surface(
                    color = Color(0xFFD1FAE5),
                    contentColor = KunuzGreenDark,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                ) {
                    Text(
                        text = treasure.grade,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CitationLabel(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = KunuzHeadline
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color(0xFF374151)
        )
    }
}

@Composable
private fun RepeatCounterBlock(
    treasure: Treasure,
    repeatCount: Int,
    counter: Int,
    onIncrement: () -> Unit,
    onOpenTasbeeh: (Treasure) -> Unit
) {
    val isDone = counter >= repeatCount
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(12.dp),
        color = KunuzSoftGreen.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, KunuzGreenBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(KunuzPrimary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${repeatCount}x",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "ورد التكرار النبوي: $repeatCount مرة",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = KunuzGreenDark
                    )
                    Text(
                        text = "أنجزت: $counter من $repeatCount",
                        fontSize = 11.sp,
                        color = KunuzPrimary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .testTag("counter-btn-${treasure.id}")
                        .clickable(enabled = !isDone, onClick = onIncrement)
                        .background(
                            color = if (isDone) KunuzGreenStrong else KunuzPrimary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isDone) "تم" else "عدّ (+١)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isDone) {
                            Icon(
                                imageVector = Lucide.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onOpenTasbeeh(treasure) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Lucide.Compass,
                        contentDescription = "فتح في المسبحة الإلكترونية الكبيرة",
                        tint = KunuzPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionBlock(treasure: Treasure) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0FDF4),
        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Lucide.Sparkles,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "كيف تعمل بهذا الكنز النبوي؟",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF166534)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = treasure.action,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Color(0xFF14532D)
            )
        }
    }
}

@Composable
private fun ExplanationBlock(
    expanded: Boolean,
    treasure: Treasure,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(onClick = onToggle)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Lucide.BookOpen,
                contentDescription = null,
                tint = KunuzBody,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "شرح الكنز وفضيلته للشيخ محمود المصري",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = KunuzBody
            )
        }
        Icon(
            imageVector = if (expanded) Lucide.ChevronUp else Lucide.ChevronDown,
            contentDescription = null,
            tint = KunuzBody,
            modifier = Modifier.size(16.dp)
        )
    }

    if (expanded) {
        Text(
            text = treasure.explanation,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = KunuzBody,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .background(Color(0xFFFDFCF9), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )
    }
}

@Composable
private fun TagsRow(tags: List<String>, onSelectTag: (String) -> Unit) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        tags.forEach { tag ->
            Row(
                modifier = Modifier
                    .clickable { onSelectTag(tag) }
                    .background(Color(0xFFF4F1EA), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Lucide.Tag,
                    contentDescription = null,
                    tint = KunuzLightGray,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = tag,
                    fontSize = 11.sp,
                    color = Color(0xFF52525B)
                )
            }
        }
    }
}

@Composable
private fun CardFooter(
    treasure: Treasure,
    isListening: Boolean,
    onToggleListen: () -> Unit
) {
    var copied by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(KunuzFooterBg)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onToggleListen)
                .background(
                    color = if (isListening) Color(0xFFFEE2E2) else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isListening) Lucide.VolumeX else Lucide.Volume2,
                contentDescription = null,
                tint = if (isListening) Color(0xFFEF4444) else KunuzPrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (isListening) "إيقاف الصوت" else "استمع للحديث",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isListening) Color(0xFFB91C1C) else Color(0xFF374151)
            )
        }

        Row(
            modifier = Modifier
                .clickable {
                    clipboardManager.setText(AnnotatedString(formatTreasureForShare(treasure)))
                    copied = true
                    scope.launch {
                        delay(2000)
                        copied = false
                    }
                }
                .background(
                    color = if (copied) Color(0xFFECFDF5) else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (copied) Lucide.Check else Lucide.Copy,
                contentDescription = null,
                tint = if (copied) Color(0xFF059669) else Color(0xFF6B7280),
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (copied) "تم النسخ بنجاح" else "نسخ الحديث كاملاً",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (copied) KunuzPrimary else Color(0xFF374151)
            )
        }
    }
}