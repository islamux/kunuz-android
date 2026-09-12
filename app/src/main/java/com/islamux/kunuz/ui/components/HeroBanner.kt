package com.islamux.kunuz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Award
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.islamux.kunuz.logic.ArabicText
import com.islamux.kunuz.ui.theme.AmiriFontFamily
import com.islamux.kunuz.ui.theme.KunuzAccent

@Composable
fun HeroBanner(
    totalTreasures: Int,
    chaptersCount: Int,
    onOpenDaily: () -> Unit,
    onOpenChecklist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF065F46))
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Lucide.Sparkles,
                    contentDescription = null,
                    tint = KunuzAccent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "الموسوعة الشاملة لكنوز السنة النبوية",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA7F3D0)
                )
            }

            Text(
                text = "١٤٠ كنزاً نبوياً من أعظم ما علّمنا رسول الله ﷺ",
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "محققة بالنص الكامل، التخريج الدقيق، وسر الفضيلة، وكيفية التطبيق العملي اليومي؛ جمعاً لما أفاض به فضيلة الشيخ محمود المصري (أبو عمار) في سلسلتي «كنوز من السنة» و«كنوز وأسرار».",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFFD1FAE5),
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .testTag("hero-daily-btn")
                        .clickable(onClick = onOpenDaily),
                    shape = RoundedCornerShape(12.dp),
                    color = KunuzAccent,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.Sparkles,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "طالع كنز اليوم المختار",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF713F12)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .testTag("hero-checklist-btn")
                        .clickable(onClick = onOpenChecklist),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.Award,
                            contentDescription = null,
                            tint = Color(0xFFA7F3D0),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "جدول الورد اليومي",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(top = 12.dp)
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatCell(value = ArabicText.toArabicDigits(totalTreasures), label = "حديثاً وكنزاً موثقاً")
                StatCell(value = ArabicText.toArabicDigits(chaptersCount), label = "أبواب فقهية وحياتية")
                StatCell(value = "١٠٠٪", label = "أحاديث صحيحة وحسنة")
                StatCell(value = "مجاني", label = "صدقة جارية لوجه الله")
            }
        }
    }
}

@Composable
private fun StatCell(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 17.sp,
            fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold,
            color = KunuzAccent
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFFD1FAE5),
            textAlign = TextAlign.Center
        )
    }
}