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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RotateCw
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Type
import com.composables.icons.lucide.X
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.logic.ArabicText
import com.islamux.kunuz.ui.theme.KunuzAmber
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark
import com.islamux.kunuz.ui.theme.KunuzText
import com.islamux.kunuz.ui.theme.TajawalFontFamily

@Composable
fun TopBar(
    fontSize: FontSize,
    totalTreasures: Int,
    showTashkeel: Boolean,
    searchQuery: String,
    onCycleFontSize: () -> Unit,
    onToggleTashkeel: () -> Unit,
    onOpenDaily: () -> Unit,
    onOpenRandom: () -> Unit,
    onOpenAbout: () -> Unit,
    onSearchChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onBrandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(color = Color(0xFFFAF8F5), modifier = modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrandBlock(
                    totalTreasures = totalTreasures,
                    onClick = onBrandClick,
                    modifier = Modifier.weight(1f)
                )

                QuickActionButtons(
                    fontSize = fontSize,
                    showTashkeel = showTashkeel,
                    onOpenDaily = onOpenDaily,
                    onOpenRandom = onOpenRandom,
                    onCycleFontSize = onCycleFontSize,
                    onToggleTashkeel = onToggleTashkeel,
                    onOpenAbout = onOpenAbout
                )
            }

            SearchField(
                searchQuery = searchQuery,
                onSearchChange = onSearchChange,
                onClearSearch = onClearSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun BrandBlock(
    totalTreasures: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(listOf(KunuzPrimary, KunuzPrimaryDark)),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Lucide.Sparkles,
                contentDescription = null,
                tint = Color(0xFFFEF08A),
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "كنوز من السنة المطهرة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF064E3B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(Modifier.width(6.dp))
                Surface(
                    color = Color(0xFFD1FAE5),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                ) {
                    Text(
                        text = "${ArabicText.toArabicDigits(totalTreasures)} كنزاً نبوياً",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = "مستخلصة ومحققة من سلاسل فضيلة الشيخ محمود المصري",
                fontSize = 11.sp,
                color = KunuzPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickActionButtons(
    fontSize: FontSize,
    showTashkeel: Boolean,
    onOpenDaily: () -> Unit,
    onOpenRandom: () -> Unit,
    onCycleFontSize: () -> Unit,
    onToggleTashkeel: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Surface(
            modifier = Modifier
                .clickable(onClick = onOpenDaily)
                .testTag("daily-treasure-btn"),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFEF3C7),
            border = BorderStroke(1.dp, Color(0xFFFCD34D).copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Lucide.Flame,
                    contentDescription = null,
                    tint = KunuzAmber,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "كنز اليوم",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF92400E)
                )
            }
        }

        TopIconBtn(
            testTag = "random-treasure-btn",
            icon = Lucide.RotateCw,
            contentDescription = "كنز عشوائي",
            bg = Color(0xFFECFDF5),
            fg = KunuzPrimary,
            border = Color(0xFFA7F3D0),
            onClick = onOpenRandom
        )

        FontSizeButton(fontSize = fontSize, onClick = onCycleFontSize)

        TashkeelButton(showTashkeel = showTashkeel, onClick = onToggleTashkeel)

        TopIconBtn(
            testTag = "about-modal-btn",
            icon = Lucide.Info,
            contentDescription = "عن التطبيق",
            bg = Color.Transparent,
            fg = Color(0xFF4B5563),
            border = Color.Transparent,
            onClick = onOpenAbout
        )
    }
}

@Composable
private fun TopIconBtn(
    testTag: String,
    icon: ImageVector,
    contentDescription: String,
    bg: Color,
    fg: Color,
    border: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .testTag(testTag)
            .clickable(onClick = onClick)
            .background(color = bg, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = border, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = fg,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun FontSizeButton(fontSize: FontSize, onClick: () -> Unit) {
    val label = when (fontSize) {
        FontSize.NORMAL -> "A"
        FontSize.LARGE -> "A+"
        FontSize.XLARGE -> "A++"
    }
    Box(
        modifier = Modifier
            .height(36.dp)
            .testTag("font-size-toggle-btn")
            .clickable(onClick = onClick)
            .background(color = Color(0xFFF4F1EA), shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color(0xFFDED8C8), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Lucide.Type,
                contentDescription = null,
                tint = Color(0xFF374151),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )
        }
    }
}

@Composable
private fun TashkeelButton(showTashkeel: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .testTag("tashkeel-toggle-btn")
            .clickable(onClick = onClick)
            .background(
                color = if (showTashkeel) KunuzPrimary else Color(0xFFF4F1EA),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (showTashkeel) KunuzPrimary else Color(0xFFDED8C8),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ًَُِ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (showTashkeel) Color.White else Color(0xFF6B7280)
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, KunuzBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Lucide.Search,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            if (searchQuery.isEmpty()) {
                Text(
                    text = "ابحث في نص الحديث، الراوي، الموضوع...",
                    fontSize = 13.sp,
                    color = Color(0xFF9CA3AF),
                    maxLines = 1
                )
            }
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = KunuzText,
                    fontFamily = TajawalFontFamily
                ),
                cursorBrush = SolidColor(KunuzPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global-search-input")
                    .padding(vertical = 10.dp)
            )
        }

        if (searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clickable(onClick = onClearSearch)
                    .padding(4.dp)
                    .testTag("clear-search-btn")
            ) {
                Icon(
                    imageVector = Lucide.X,
                    contentDescription = "مسح البحث",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}