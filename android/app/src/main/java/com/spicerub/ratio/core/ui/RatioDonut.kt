package com.spicerub.ratio.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.theme.GoldRim
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.SurfaceMuted
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.domain.model.RatioSegment

@Composable
fun RatioDonut(
    segments: List<RatioSegment>,
    modifier: Modifier = Modifier,
    diameter: Dp = 196.dp,
    strokeWidth: Dp = 40.dp,
    centerValue: String? = null,
    centerLabel: String? = null,
    description: String? = null
) {
    val animated = segments.map { segment ->
        key(segment.id) {
            val value by animateFloatAsState(
                targetValue = segment.weight.coerceAtLeast(0f),
                animationSpec = tween(GameConfig.DONUT_MORPH_MS, easing = FastOutSlowInEasing),
                label = "donutSweep"
            )
            segment.colorHex to value
        }
    }
    val total = animated.sumOf { it.second.toDouble() }.toFloat()

    Box(
        modifier = modifier
            .size(diameter)
            .clearAndSetSemantics { if (description != null) contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val inset = strokePx / 2f
            val arcSize = Size(size.width - strokePx, size.height - strokePx)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = SurfaceMuted,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Butt)
            )
            if (total > 0f) {
                var start = -90f
                animated.forEach { entry ->
                    val sweep = entry.second / total * 360f
                    if (sweep > GAP) {
                        drawArc(
                            color = Color(entry.first),
                            startAngle = start + GAP / 2f,
                            sweepAngle = sweep - GAP,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokePx, cap = StrokeCap.Butt)
                        )
                    }
                    start += sweep
                }
            }
            drawCircle(
                color = GoldRim,
                radius = size.minDimension / 2f - 1.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = GoldRim.copy(alpha = 0.45f),
                radius = size.minDimension / 2f - strokePx + 1.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
        }
        if (centerValue != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = centerValue,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Paprika
                )
                if (centerLabel != null) {
                    Text(
                        text = centerLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

private const val GAP = 2.5f
