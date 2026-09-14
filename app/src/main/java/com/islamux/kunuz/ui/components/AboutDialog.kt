package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.X
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark

private data class ChapterListItem(
    val emoji: String,
    val name: String,
    val description: String,
    val bg: Color,
    val border: Color
)

private val CHAPTER_ITEMS = listOf(
    ChapterListItem("🌿", "أذكار اليوم والليلة:", "تحصين الصباح والمساء واليوم.", Color(0xFFF0FDF4), Color(0xFFBBF7D0)),
    ChapterListItem("🕌", "الصلاة والمساجد:", "السنن الرواتب، الجماعة، التهجد.", Color(0xFFEFF6FF), Color(0xFFBFDBFE)),
    ChapterListItem("🕊️", "مغفرة الذنوب والتوبة:", "الاستغفار والمكفرات الكبرى.", Color(0xFFFEFCE8), Color(0xFFFEF08A)),
    ChapterListItem("🛡️", "تفريج الكروب والرقية:", "أسلحة الشدائد والشفاء.", Color(0xFFFDF2F8), Color(0xFFFBCFE8)),
    ChapterListItem("📖", "فضائل القرآن الكريم:", "السور المنجية والآيات الكوافل.", Color(0xFFECFDF5), Color(0xFFA7F3D0)),
    ChapterListItem("🤝", "الأخلاق وصلة الأرحام:", "بر الوالدين وتفريج كرب الناس.", Color(0xFFFAF5FF), Color(0xFFE9D5FF)),
    ChapterListItem("💧", "الصدقات والأجور الجارية:", "سقي الماء والسبع الجارية.", Color(0xFFFFF7ED), Color(0xFFFED7AA)),
    ChapterListItem("🌙", "الصيام ومواسم الخيرات:", "عرفة، عاشوراء، وست شوال.", Color(0xFFF0FDFA), Color(0xFF99F6E4))
)

@Composable
fun AboutDialog(onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(16.dp)
                .testTag("about-dialog"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, KunuzBorder)
        ) {
            Column {
                DialogHeader(
                    colors = listOf(KunuzPrimary, KunuzPrimaryDark),
                    icon = Lucide.Sparkles,
                    title = "عن تطبيق كنوز من السنة النبوية",
                    onClose = onClose
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Spacer(Modifier.height(4.dp))
                    IntroBox()
                    ChaptersList()
                    AdviceBox()
                    ClosingText()
                    Spacer(Modifier.height(4.dp))
                }

                DialogFooter {
                    Surface(
                        modifier = Modifier.clickable(onClick = onClose),
                        shape = RoundedCornerShape(12.dp),
                        color = KunuzPrimary
                    ) {
                        Text(
                            text = "حفظكم الله وجزاكم خيراً",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IntroBox() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFFAF7F0),
        border = BorderStroke(1.dp, KunuzBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "موسوعة جامعة لـ ١٤٠ كنزاً نبوياً أصيلاً",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF065F46)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    append("هذا التطبيق ثمرة حصر وفهرسة دقيقة لأحاديث سلسلتي ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("«كنوز من السنة»") }
                    append(" و")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("«كنوز وأسرار»") }
                    append(" لفضيلة الشيخ الداعية ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("محمود المصري (أبو عمار)") }
                    append(" حفظه الله ونفع بعلمه، مع التوثيق المباشر بنص الحديث، الراوي، المخرج، الدرجة، والشرح العملي الميسر.")
                },
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF4B5563)
            )
        }
    }
}

@Composable
private fun ChaptersList() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Lucide.Layers,
                contentDescription = null,
                tint = KunuzPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "الأبواب التسعة للموسوعة:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }
        Spacer(Modifier.height(8.dp))
        CHAPTER_ITEMS.forEach { item ->
            ChapterListItemRow(item)
        }
    }
}

@Composable
private fun ChapterListItemRow(item: ChapterListItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(10.dp),
        color = item.bg,
        border = BorderStroke(1.dp, item.border)
    ) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(
                text = item.emoji,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 1.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(item.name) }
                    append(" ")
                    append(item.description)
                },
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = Color(0xFF374151)
            )
        }
    }
}

@Composable
private fun AdviceBox() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFECFDF5),
        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Lucide.CircleCheck,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "نصيحة الشيخ محمود المصري في العمل بالكنوز:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "«لا تشترط على نفسك أن تطبق كل الكنوز في يوم واحد فتصاب بالفتور؛ بل خذ كل أسبوع كنزاً أو كنزين واجعلهما عادة راسخة في يومك وليلتك، وتذكر قول حبيبك المصطفى ﷺ: \"أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ\".»",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF065F46)
            )
        }
    }
}

@Composable
private fun ClosingText() {
    Text(
        text = "نسأل الله أن يجعل هذا العمل خالصاً لوجهه الكريم، وأن يتقبل من فضيلة الشيخ محمود المصري ومن كل من ساهم في نشره وتعلمه والعمل به.",
        fontSize = 11.sp,
        lineHeight = 16.sp,
        color = Color(0xFF6B7280),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}

@Composable
internal fun DialogHeader(
    colors: List<Color>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClose: () -> Unit,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = Brush.linearGradient(colors))
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFFEF08A),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            subtitle?.let {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = it,
                    fontSize = 11.sp,
                    color = Color(0xFFFFF3CD)
                )
            }
        }
        Surface(
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onClose)
                .testTag("dialog-close-btn"),
            shape = RoundedCornerShape(50),
            color = Color.White.copy(alpha = 0.15f)
        ) {
            Icon(
                imageVector = Lucide.X,
                contentDescription = "إغلاق",
                tint = Color.White,
                modifier = Modifier.padding(7.dp)
            )
        }
    }
}

@Composable
internal fun DialogFooter(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAF8F5))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        content()
    }
}