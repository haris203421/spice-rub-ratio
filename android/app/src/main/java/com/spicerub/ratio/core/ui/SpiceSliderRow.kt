package com.spicerub.ratio.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.theme.SurfaceCard
import com.spicerub.ratio.core.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun SpiceSliderRow(
    name: String,
    partsLabel: String,
    parts: Float,
    tint: Color,
    badge: String?,
    badgeTint: Color,
    removeLabel: String,
    stateLabel: String,
    onPartsChange: (Double) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val dotSize by animateDpAsState(
        targetValue = if (parts > 0f) 12.dp else 10.dp,
        animationSpec = tween(140),
        label = "spiceDot"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.22f)),
        modifier = modifier
            .fillMaxWidth()
            .semantics { stateDescription = stateLabel }
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 6.dp, top = 10.dp, bottom = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .background(tint, CircleShape)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 10.dp)
                )
                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(badgeTint.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeTint
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = partsLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = tint
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Close,
                        contentDescription = removeLabel,
                        tint = badgeTint.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Slider(
                value = parts,
                onValueChange = { raw ->
                    val snapped = (raw / GameConfig.PART_STEP.toFloat()).roundToInt() *
                        GameConfig.PART_STEP.toFloat()
                    if (snapped != parts) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onPartsChange(snapped.toDouble())
                    }
                },
                valueRange = GameConfig.PART_MIN..GameConfig.PART_MAX,
                steps = GameConfig.PART_STEPS,
                colors = SliderDefaults.colors(
                    thumbColor = tint,
                    activeTrackColor = tint,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    inactiveTrackColor = Color(0x33A2552F)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun SpiceAmountRow(
    name: String,
    amount: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(tint, CircleShape)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium,
            color = tint
        )
    }
}
