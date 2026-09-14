package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.ui.theme.KunuzBorder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveFiltersBar(
    selectedChapter: ChapterId?,
    selectedTag: String?,
    searchQuery: String,
    resultCount: Int,
    chapters: List<Chapter>,
    onClearChapter: () -> Unit,
    onClearSearch: () -> Unit,
    onClearTag: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ActiveFiltersBar"),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, KunuzBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "عوامل التصفية النشطة:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                selectedChapter?.let { id ->
                    val chapterName = chapters.find { it.id == id }?.name ?: id.value
                    ActiveFilterChip(
                        label = "الباب: $chapterName",
                        bg = Color(0xFFECFDF5),
                        fg = Color(0xFF065F46),
                        borderColor = Color(0xFFA7F3D0),
                        onClear = onClearChapter
                    )
                }

                if (searchQuery.isNotBlank()) {
                    ActiveFilterChip(
                        label = "البحث: \"$searchQuery\"",
                        bg = Color(0xFFEFF6FF),
                        fg = Color(0xFF1E40AF),
                        borderColor = Color(0xFFBFDBFE),
                        onClear = onClearSearch
                    )
                }

                selectedTag?.let { tag ->
                    ActiveFilterChip(
                        label = "الوسم: #$tag",
                        bg = Color(0xFFFEF3C7),
                        fg = Color(0xFF92400E),
                        borderColor = Color(0xFFFDE68A),
                        onClear = onClearTag
                    )
                }

                Text(
                    text = "(وجدنا $resultCount كنزاً)",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable(onClick = onClearAll),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "إلغاء التصفية",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDC2626),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    bg: Color,
    fg: Color,
    borderColor: Color,
    onClear: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(bg, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = fg,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .clickable(onClick = onClear)
                .padding(2.dp)
        ) {
            Icon(
                imageVector = Lucide.X,
                contentDescription = "إزالة التصفية",
                tint = fg,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}