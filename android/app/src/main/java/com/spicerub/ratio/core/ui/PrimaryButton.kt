package com.spicerub.ratio.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.theme.HighlightInner
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.PaprikaDark
import com.spicerub.ratio.core.theme.PaprikaLight
import com.spicerub.ratio.core.theme.TextOnPrimary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: Dp = 60.dp,
    subtitle: String? = null,
    disabledHint: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = GameConfig.PRESS_DAMPING,
            stiffness = GameConfig.PRESS_STIFFNESS
        ),
        label = "primaryButtonScale"
    )
    val shape = RoundedCornerShape(18.dp)
    val gradient = if (enabled) {
        Brush.horizontalGradient(listOf(PaprikaLight, Paprika, PaprikaDark))
    } else {
        Brush.horizontalGradient(
            listOf(
                PaprikaLight.copy(alpha = 0.38f),
                Paprika.copy(alpha = 0.38f),
                PaprikaDark.copy(alpha = 0.38f)
            )
        )
    }
    val semanticsModifier = if (!enabled && disabledHint != null) {
        Modifier.semantics { stateDescription = disabledHint }
    } else {
        Modifier
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextOnPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextOnPrimary.copy(alpha = 0.6f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 14.dp else 0.dp,
                shape = shape,
                ambientColor = Paprika,
                spotColor = Paprika
            )
            .background(brush = gradient, shape = shape)
            .border(1.dp, HighlightInner, shape)
            .then(semanticsModifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextOnPrimary.copy(alpha = 0.82f)
                )
            }
        }
    }
}
