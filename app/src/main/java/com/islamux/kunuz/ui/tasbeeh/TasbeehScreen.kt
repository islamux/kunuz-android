package com.islamux.kunuz.ui.tasbeeh

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Volume2
import com.composables.icons.lucide.VolumeX
import com.composables.icons.lucide.X
import com.islamux.kunuz.data.Tasbeeh
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.sound.playClick
import com.islamux.kunuz.ui.components.ConfettiController
import com.islamux.kunuz.ui.components.ConfettiOverlay
import com.islamux.kunuz.ui.theme.AmiriFontFamily
import com.islamux.kunuz.ui.theme.KunuzPrimary

private val TasbeehHeaderStart = Color(0xFF047857)
private val TasbeehHeaderEnd = Color(0xFF065F46)
private val TasbeehInk = Color(0xFF1F2937)
private val TasbeehMuted = Color(0xFF4B5563)
private val TasbeehFaint = Color(0xFF9CA3AF)
private val TasbeehCardBg = Color(0xFFFAF7F0)
private val TasbeehCardBorder = Color(0xFFE8E2D5)
private val TasbeehChipBg = Color(0xFFF4F1EA)
private val TasbeehCircleBorder = Color(0x66D1FAE5)
private val TasbeehCircleStart = Color(0xFF065F46)
private val TasbeehCircleMid = Color(0xFF047857)
private val TasbeehCircleEnd = Color(0xFF059669)
private val TasbeehAccentBg = Color(0xFFFEF08A)
private val TasbeehAccentText = Color(0xFF854D0E)
private val TasbeehAccentBorder = Color(0xFFFDE047)
private val TasbeehProgressTrack = Color(0xFFE5E7EB)
private val TasbeehDivider = Color(0xFFF0ECE1)
private val TasbeehDanger = Color(0xFFDC2626)

private val TASBEEH_TARGETS = listOf(10, 33, 100, 1000)

@Composable
fun TasbeehScreen(treasure: Treasure?, onClose: () -> Unit) {
    var count by remember { mutableStateOf(0) }
    var target by remember { mutableStateOf(Tasbeeh.defaultTarget(treasure)) }
    var soundEnabled by remember { mutableStateOf(true) }
    val confettiController = remember { ConfettiController() }
    val context = LocalContext.current

    LaunchedEffect(treasure) {
        count = 0
        target = Tasbeeh.defaultTarget(treasure)
    }

    Dialog(onDismissRequest = onClose) {
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.96f)
                    .testTag("tasbeeh-screen"),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White
            ) {
                Column {
                    TasbeehHeader(onClose = onClose)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TreasureSummary(treasure = treasure)
                        TargetSelector(
                            target = target,
                            onSelect = { target = it }
                        )
                        CounterBead(
                            count = count,
                            target = target,
                            onTap = {
                                val newCount = count + 1
                                count = newCount
                                dispatchTasbeehEvent(
                                    event = Tasbeeh.eventAfterIncrement(
                                        newCount = newCount,
                                        target = target,
                                        soundEnabled = soundEnabled
                                    ),
                                    context = context,
                                    confettiController = confettiController
                                )
                            }
                        )
                        ProgressBar(count = count, target = target)
                        TasbeehFooter(
                            soundEnabled = soundEnabled,
                            onToggleSound = { soundEnabled = !soundEnabled },
                            onReset = { count = 0 }
                        )
                    }
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
private fun TasbeehHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(TasbeehHeaderStart, TasbeehHeaderEnd))
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Lucide.Sparkles,
                contentDescription = null,
                tint = Color(0xFFFEF08A),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "المسبحة والعداد التفاعلي",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClose)
                .testTag("tasbeeh-close"),
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
private fun TreasureSummary(treasure: Treasure?) {
    if (treasure != null) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tasbeeh-treasure-card"),
            shape = RoundedCornerShape(14.dp),
            color = TasbeehCardBg,
            border = BorderStroke(1.dp, TasbeehCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "الكنز #${treasure.id}: ${treasure.title}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KunuzPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "«${treasure.hadith}»",
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    fontFamily = AmiriFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = TasbeehInk,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Text(
            text = "سبحان الله وبحمده، أستغفر الله وأتوب إليه",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TasbeehMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun TargetSelector(target: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "الهدف:",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TasbeehMuted
        )
        TASBEEH_TARGETS.forEach { value ->
            val selected = value == target
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelect(value) }
                    .background(if (selected) KunuzPrimary else TasbeehChipBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("tasbeeh-target-$value")
            ) {
                Text(
                    text = "$value",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White else TasbeehMuted
                )
            }
        }
    }
}

@Composable
private fun CounterBead(count: Int, target: Int, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .size(192.dp)
            .testTag("tasbeeh-tap-area")
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(TasbeehCircleStart, TasbeehCircleMid, TasbeehCircleEnd)
                )
            )
            .border(4.dp, TasbeehCircleBorder, CircleShape)
            .clickable(onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "اضغط للعدّ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFD1FAE5)
            )
            Text(
                text = "$count",
                fontSize = 46.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "من $target",
                fontSize = 12.sp,
                color = Color(0xFFA7F3D0)
            )
        }

        if (count >= target) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .size(36.dp)
                    .background(TasbeehAccentBg, CircleShape)
                    .border(1.dp, TasbeehAccentBorder, CircleShape)
                    .testTag("tasbeeh-complete-badge"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Lucide.CircleCheck,
                    contentDescription = "اكتمل العدد",
                    tint = TasbeehAccentText,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(count: Int, target: Int) {
    val fraction = if (target <= 0) 0f else (count.toFloat() / target).coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 300),
        label = "tasbeehProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 24.dp)
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(TasbeehProgressTrack)
            .testTag("tasbeeh-progress-bar")
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedFraction)
                .background(KunuzPrimary)
        )
    }
}

@Composable
private fun TasbeehFooter(
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(TasbeehDivider)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onToggleSound)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("tasbeeh-sound-toggle"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (soundEnabled) Lucide.Volume2 else Lucide.VolumeX,
                    contentDescription = null,
                    tint = if (soundEnabled) KunuzPrimary else TasbeehFaint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (soundEnabled) "الصوت مفعّل" else "الصوت مكتوم",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TasbeehMuted
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onReset)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("tasbeeh-reset"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Lucide.RotateCcw,
                    contentDescription = null,
                    tint = TasbeehDanger,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "إعادة التصفير",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TasbeehDanger
                )
            }
        }
    }
}

private fun dispatchTasbeehEvent(
    event: Tasbeeh.Event,
    context: Context,
    confettiController: ConfettiController
) {
    if (event.sound) playClick(context, enabled = true)
    if (event.haptic) vibrate(context, durationMs = 25)
    if (event.confetti) confettiController.fire()
}

private fun vibrate(context: Context, durationMs: Long) {
    runCatching {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if (vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, 255))
        }
    }
}
