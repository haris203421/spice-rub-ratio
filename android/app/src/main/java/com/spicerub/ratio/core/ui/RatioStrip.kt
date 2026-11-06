package com.spicerub.ratio.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.theme.GoldRim
import com.spicerub.ratio.core.theme.SurfaceMuted
import com.spicerub.ratio.domain.model.RatioSegment

@Composable
fun RatioStrip(
    segments: List<RatioSegment>,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    corner: Dp = 4.dp,
    showRim: Boolean = false
) {
    val shape = RoundedCornerShape(corner)
    val base = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(shape)
        .background(SurfaceMuted)
    val framed = if (showRim) base.border(1.dp, GoldRim.copy(alpha = 0.5f), shape) else base

    Box(modifier = modifier.then(framed)) {
        Row(modifier = Modifier.fillMaxSize()) {
            segments.forEach { segment ->
                key(segment.id) {
                    val weight by animateFloatAsState(
                        targetValue = segment.weight.coerceAtLeast(0.01f),
                        animationSpec = tween(GameConfig.DONUT_MORPH_MS, easing = FastOutSlowInEasing),
                        label = "stripWeight"
                    )
                    Box(
                        modifier = Modifier
                            .weight(weight)
                            .fillMaxHeight()
                            .background(Color(segment.colorHex))
                    )
                }
            }
        }
    }
}
