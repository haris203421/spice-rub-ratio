package com.spicerub.ratio.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.theme.SurfaceCard
import com.spicerub.ratio.core.theme.SurfaceMuted
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun RotaryDial(
    value: Double,
    minValue: Double,
    maxValue: Double,
    step: Double,
    tint: Color,
    icon: Painter,
    iconDescription: String,
    iconSize: Dp,
    stateLabel: String,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    diameter: Dp = 188.dp
) {
    val density = LocalDensity.current
    val diameterPx = with(density) { diameter.toPx() }
    val latestChange by rememberUpdatedState(onValueChange)
    val span = (maxValue - minValue).coerceAtLeast(step)
    val fraction = ((value - minValue) / span).toFloat().coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(GameConfig.DIAL_SNAP_MS),
        label = "dialSweep"
    )
    val animatedIcon by animateDpAsState(
        targetValue = iconSize,
        animationSpec = tween(GameConfig.DIAL_SNAP_MS),
        label = "dialIcon"
    )

    fun applyFromPosition(position: Offset) {
        val dx = position.x - diameterPx / 2f
        val dy = position.y - diameterPx / 2f
        val degrees = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())) + 360.0) % 360.0
        val travel = (degrees - START_ANGLE + 360.0) % 360.0
        if (travel > SWEEP_ANGLE + DEAD_ZONE) return
        val raw = minValue + (travel.coerceAtMost(SWEEP_ANGLE) / SWEEP_ANGLE) * span
        val snapped = (raw / step).roundToInt() * step
        val clamped = snapped.coerceIn(minValue, maxValue)
        if (clamped != value) latestChange(clamped)
    }

    Box(
        modifier = modifier
            .size(diameter)
            .semantics { stateDescription = stateLabel }
            .pointerInput(minValue, maxValue, step) {
                detectDragGestures { change, _ -> applyFromPosition(change.position) }
            }
            .pointerInput(minValue, maxValue, step) {
                detectTapGestures { position -> applyFromPosition(position) }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = 20.dp.toPx()
            val inset = strokePx / 2f + 4.dp.toPx()
            val arcSize = Size(size.width - inset * 2f, size.height - inset * 2f)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = SurfaceMuted,
                startAngle = START_ANGLE.toFloat(),
                sweepAngle = SWEEP_ANGLE.toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                color = tint,
                startAngle = START_ANGLE.toFloat(),
                sweepAngle = SWEEP_ANGLE.toFloat() * animatedFraction,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            val knobAngle = Math.toRadians(START_ANGLE + SWEEP_ANGLE * animatedFraction)
            val radius = arcSize.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val knob = Offset(
                center.x + (cos(knobAngle) * radius).toFloat(),
                center.y + (sin(knobAngle) * radius).toFloat()
            )
            drawCircle(color = SurfaceCard, radius = 14.dp.toPx(), center = knob)
            drawCircle(
                color = tint,
                radius = 14.dp.toPx(),
                center = knob,
                style = Stroke(width = 3.dp.toPx())
            )
            val grip = 5.dp.toPx()
            for (offset in -1..1) {
                val dxGrip = offset * 3.dp.toPx()
                drawLine(
                    color = tint,
                    start = Offset(knob.x + dxGrip, knob.y - grip),
                    end = Offset(knob.x + dxGrip, knob.y + grip),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
        Image(
            painter = icon,
            contentDescription = iconDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(animatedIcon)
        )
    }
}

private const val START_ANGLE = 135.0
private const val SWEEP_ANGLE = 270.0
private const val DEAD_ZONE = 30.0
