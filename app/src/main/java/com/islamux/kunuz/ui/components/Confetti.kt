package com.islamux.kunuz.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.testTag
import com.islamux.kunuz.ui.theme.KunuzAccent
import com.islamux.kunuz.ui.theme.KunuzAmber
import com.islamux.kunuz.ui.theme.KunuzPrimary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ConfettiController {
    private val _trigger = MutableStateFlow(0L)
    val trigger: StateFlow<Long> = _trigger.asStateFlow()

    fun fire() {
        _trigger.update { it + 1 }
    }
}

private const val CONFETTI_DURATION_MS = 1200

private val confettiPalette = listOf(
    KunuzPrimary,
    KunuzAccent,
    KunuzAmber,
    Color(0xFFF43F5E),
    Color(0xFF3B82F6),
    Color(0xFF22C55E)
)

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val driftX: Float,
    val fallY: Float,
    val radius: Float,
    val color: Color
)

private fun sampleParticles(seed: Long): List<ConfettiParticle> {
    val random = Random(seed)
    return List(60) {
        ConfettiParticle(
            startX = random.nextFloat(),
            startY = random.nextFloat() * 0.4f,
            driftX = (random.nextFloat() - 0.5f) * 0.4f,
            fallY = 0.5f + random.nextFloat() * 0.5f,
            radius = 3f + random.nextFloat() * 4f,
            color = confettiPalette[random.nextInt(confettiPalette.size)]
        )
    }
}

@Composable
fun ConfettiOverlay(
    controller: ConfettiController,
    modifier: Modifier = Modifier
) {
    val triggerValue by controller.trigger.collectAsState()
    var particles by remember { mutableStateOf<List<ConfettiParticle>>(emptyList()) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(triggerValue) {
        if (triggerValue == 0L) {
            particles = emptyList()
            progress.snapTo(0f)
        } else {
            particles = sampleParticles(triggerValue)
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(durationMillis = CONFETTI_DURATION_MS))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("confettiOverlay"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("confettiCanvas")
        ) {
            confettiParticles(particles, progress.value)
        }
    }
}

private fun DrawScope.confettiParticles(particles: List<ConfettiParticle>, progress: Float) {
    if (particles.isEmpty() || progress <= 0f) return
    particles.forEach { particle ->
        val x = particle.startX * size.width + progress * particle.driftX * size.width
        val y = particle.startY * size.height + progress * particle.fallY * size.height
        val alpha = (1f - progress).coerceIn(0f, 1f)
        drawPoints(
            points = listOf(Offset(x, y)),
            pointMode = PointMode.Points,
            color = particle.color.copy(alpha = alpha),
            strokeWidth = particle.radius * 2f,
            cap = StrokeCap.Round
        )
    }
}