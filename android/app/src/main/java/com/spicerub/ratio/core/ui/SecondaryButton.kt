package com.spicerub.ratio.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spicerub.ratio.core.theme.Cardamom
import com.spicerub.ratio.core.theme.SurfaceCard

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(18.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.5.dp, if (enabled) Cardamom else Cardamom.copy(alpha = 0.38f)),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceCard,
            contentColor = Cardamom,
            disabledContainerColor = SurfaceCard.copy(alpha = 0.6f),
            disabledContentColor = Cardamom.copy(alpha = 0.38f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(elevation = 4.dp, shape = shape, ambientColor = Cardamom, spotColor = Cardamom)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
