package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.logic.ArabicText
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark

@Composable
fun ChaptersGrid(
    chapters: List<Chapter>,
    chapterCounts: Map<ChapterId, Int>,
    totalTreasures: Int,
    selectedChapter: ChapterId?,
    onSelectChapter: (ChapterId?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "فهرس الأبواب والموضوعات",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF064E3B)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "اختر باباً لاستعراض جميع كنوزه وأحاديثه الخاصة",
                fontSize = 12.sp,
                color = Color(0xFF4B5563)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .testTag("chapters-grid"),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item(key = "all") {
                ChapterGridCard(
                    icon = { Icon(Lucide.Sparkles, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(24.dp)) },
                    title = "جميع الكنوز النبوية",
                    description = "استعراض شامل لجميع الأحاديث والكنوز من السلسلتين",
                    countLabel = "${ArabicText.toArabicDigits(totalTreasures)} كنزاً",
                    selected = selectedChapter == null,
                    onClick = { onSelectChapter(null) }
                )
            }

            items(chapters, key = { it.id }) { chapter ->
                ChapterGridCard(
                    icon = { ChapterIcon(icon = chapter.icon, modifier = Modifier.size(24.dp)) },
                    title = chapter.name,
                    description = chapter.description,
                    countLabel = "${ArabicText.toArabicDigits(chapterCounts[chapter.id] ?: 0)} كنزاً",
                    selected = selectedChapter == chapter.id,
                    onClick = { onSelectChapter(chapter.id) }
                )
            }
        }
    }
}

@Composable
private fun ChapterGridCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    countLabel: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val fg = if (selected) Color.White else Color(0xFF1F2937)
    val sub = if (selected) Color(0xFFA7F3D0) else Color(0xFF6B7280)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) KunuzPrimary else Color.White,
        border = BorderStroke(1.dp, if (selected) KunuzPrimary else KunuzBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }

                Surface(
                    color = if (selected) Color.White.copy(alpha = 0.2f) else Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = countLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else Color(0xFF374151),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = fg,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = sub,
                lineHeight = 16.sp,
                maxLines = 2,
                textAlign = TextAlign.Start
            )
        }
    }
}