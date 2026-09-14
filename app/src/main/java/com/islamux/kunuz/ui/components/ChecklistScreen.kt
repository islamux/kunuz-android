package com.islamux.kunuz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.Award
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.X
import com.islamux.kunuz.data.model.DEFAULT_DAILY_TASKS
import com.islamux.kunuz.data.model.SunnahTask
import com.islamux.kunuz.logic.countCompleted
import com.islamux.kunuz.ui.theme.AmiriFontFamily

private val ChecklistHeaderStart = Color(0xFF047857)
private val ChecklistHeaderEnd = Color(0xFF065F46)
private val ChecklistInk = Color(0xFF1F2937)
private val ChecklistMuted = Color(0xFF6B7280)
private val ChecklistFaint = Color(0xFF9CA3AF)
private val ChecklistBarBg = Color(0xFFFAF8F5)
private val ChecklistBarBorder = Color(0xFFE8E2D5)
private val ChecklistBadgeBg = Color(0xFFECFDF5)
private val ChecklistBadgeBorder = Color(0xFFA7F3D0)
private val ChecklistBadgeText = Color(0xFF047857)
private val ChecklistTrack = Color(0xFFE5E7EB)
private val ChecklistFlameBg = Color(0xFFFEF3C7)
private val ChecklistFlameBorder = Color(0xFFFDE68A)
private val ChecklistFlameText = Color(0xFF92400E)
private val ChecklistFlameIcon = Color(0xFFD97706)
private val ChecklistDoneBg = Color(0xFFF0FDF4)
private val ChecklistDoneBorder = Color(0xFF86EFAC)
private val ChecklistDoneIcon = Color(0xFF16A34A)
private val ChecklistDoneTitle = Color(0xFF15803D)
private val ChecklistChipBg = Color(0xFFF4F1EA)
private val ChecklistChipText = Color(0xFF52525B)
private val ChecklistDanger = Color(0xFFDC2626)

@Composable
fun ChecklistScreen(
    tasks: Map<String, Boolean>,
    streak: Int,
    onToggle: (String) -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit
) {
    val confettiController = remember { ConfettiController() }
    val taskIds = DEFAULT_DAILY_TASKS.map { it.id }
    val completedCount = countCompleted(tasks)
    val allDone = taskIds.all { tasks[it] == true }
    var wasAllDone by remember { mutableStateOf(allDone) }

    LaunchedEffect(allDone) {
        if (allDone && !wasAllDone) confettiController.fire()
        wasAllDone = allDone
    }

    Dialog(onDismissRequest = onClose) {
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.96f)
                    .testTag("checklist-screen"),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White
            ) {
                Column {
                    ChecklistHeader(onClose = onClose)
                    ProgressAndStreak(
                        completedCount = completedCount,
                        totalCount = taskIds.size,
                        streak = streak
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DEFAULT_DAILY_TASKS.forEach { task ->
                            ChecklistTaskRow(
                                task = task,
                                isDone = tasks[task.id] == true,
                                onToggle = { onToggle(task.id) }
                            )
                        }
                    }
                    ChecklistFooter(onReset = onReset)
                }
            }
            ConfettiOverlay(
                controller = confettiController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ChecklistHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(ChecklistHeaderStart, ChecklistHeaderEnd))
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Lucide.Award,
                contentDescription = null,
                tint = Color(0xFFFEF08A),
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = "الورد اليومي للسنن النبوية",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AmiriFontFamily
                )
                Text(
                    text = "جدول تطبيقي للمحافظة على أجور الكنوز يوماً بيوم",
                    color = Color(0xFFD1FAE5),
                    fontSize = 11.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClose)
                .testTag("checklist-close"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Lucide.X,
                contentDescription = "إغلاق",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProgressAndStreak(completedCount: Int, totalCount: Int, streak: Int) {
    val percentage = if (totalCount == 0) 0 else Math.round(completedCount * 100f / totalCount)
    val fraction = if (totalCount == 0) 0f else (completedCount.toFloat() / totalCount).coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 300),
        label = "checklistProgress"
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChecklistBarBg)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ChecklistBadgeBg, RoundedCornerShape(12.dp))
                        .border(1.dp, ChecklistBadgeBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$percentage%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ChecklistBadgeText
                    )
                }
                Column {
                    Text(
                        text = "إنجاز اليوم: $completedCount من $totalCount سنن",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChecklistInk
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(144.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(ChecklistTrack)
                            .testTag("checklist-progress-bar")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(animatedFraction)
                                .background(ChecklistBadgeText)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ChecklistFlameBg)
                    .border(1.dp, ChecklistFlameBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Lucide.Flame,
                    contentDescription = null,
                    tint = ChecklistFlameIcon,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "حماسة $streak أيام",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChecklistFlameText
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ChecklistBarBorder)
        )
    }
}

@Composable
private fun ChecklistTaskRow(
    task: SunnahTask,
    isDone: Boolean,
    onToggle: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isDone) ChecklistDoneBg else Color.White)
            .border(1.dp, if (isDone) ChecklistDoneBorder else ChecklistBarBorder, shape)
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .testTag("checklist-task-${task.id}"),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (isDone) Lucide.CircleCheck else Lucide.Circle,
            contentDescription = if (isDone) "تم الإنجاز" else null,
            tint = if (isDone) ChecklistDoneIcon else ChecklistFaint,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) ChecklistDoneTitle else ChecklistInk,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = task.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ChecklistChipText,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ChecklistChipBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Text(
                text = "✨ الفضل: ${task.reward}",
                fontSize = 11.sp,
                color = ChecklistMuted
            )
        }
    }
}

@Composable
private fun ChecklistFooter(onReset: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ChecklistBarBorder)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChecklistBarBg)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "«أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ»",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = AmiriFontFamily,
                color = ChecklistMuted
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onReset)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("checklist-reset"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Lucide.RotateCcw,
                    contentDescription = null,
                    tint = ChecklistDanger,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "تصفير اليوم",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChecklistDanger
                )
            }
        }
    }
}
