package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary

@Composable
fun EmptyState(
    onClearFilters: () -> Unit,
    totalTreasures: Int = 140,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .testTag("EmptyState"),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, KunuzBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF4F1EA), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Lucide.Search,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "لم نعثر على كنوز مطابقة لبحثك",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "جرب تغيير كلمات البحث، أو إزالة التشكيل، أو إلغاء تصفية الأبواب لإظهار جميع الكنوز.",
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, end = 24.dp)
            )

            Surface(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable(onClick = onClearFilters),
                shape = RoundedCornerShape(12.dp),
                color = KunuzPrimary
            ) {
                Text(
                    text = "عرض جميع الكنوز ($totalTreasures كنزاً)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
    }
}