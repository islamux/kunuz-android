package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterIconName
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark

@Composable
fun ChapterChips(
    chapters: List<Chapter>,
    chapterCounts: Map<ChapterId, Int>,
    totalCount: Int,
    selectedChapter: ChapterId?,
    onSelect: (ChapterId?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item(key = "all") {
            ChapterChip(
                label = "الكل",
                count = totalCount,
                selected = selectedChapter == null,
                onSelect = { onSelect(null) }
            )
        }

        items(chapters, key = { it.id }) { chapter ->
            ChapterChip(
                label = chapter.shortName,
                count = chapterCounts[chapter.id] ?: 0,
                icon = chapter.icon,
                selected = selectedChapter == chapter.id,
                onSelect = { onSelect(chapter.id) }
            )
        }
    }
}

@Composable
private fun ChapterChip(
    label: String,
    count: Int,
    selected: Boolean,
    onSelect: () -> Unit,
    icon: ChapterIconName? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .testTag("chapterChip")
            .border(
                width = 1.dp,
                color = if (selected) KunuzPrimaryDark else Color(0xFFE2DCCF),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (selected) KunuzPrimaryDark else Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        if (icon != null) {
            ChapterIcon(icon = icon, modifier = Modifier.size(14.dp))
        }
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color(0xFF4B5563)
        )
        Row(
            modifier = Modifier
                .background(
                    color = if (selected) Color.White.copy(alpha = 0.2f) else Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = "$count",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFF6B7280)
            )
        }
    }
}