package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Lucide

@Composable
fun FavoritesHeader(
    shownCount: Int,
    totalFavorites: Int,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("FavoritesHeader"),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFF1F2),
        border = BorderStroke(1.dp, Color(0xFFFECDD3))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Lucide.Bookmark,
                    contentDescription = null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "الكنوز المحفوظة في المفضلة ($shownCount)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9F1239)
                    )
                    Text(
                        text = "أحاديثك المختارة للرجوع إليها ومداومة العمل بها",
                        fontSize = 12.sp,
                        color = Color(0xFFBE123C)
                    )
                }
            }

            if (totalFavorites > 0) {
                Text(
                    text = "تفريغ المفضلة",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFBE123C),
                    modifier = Modifier
                        .clickable(onClick = onClearAll)
                        .padding(4.dp)
                )
            }
        }
    }
}