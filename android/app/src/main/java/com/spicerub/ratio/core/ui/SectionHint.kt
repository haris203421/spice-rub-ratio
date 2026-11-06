package com.spicerub.ratio.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.spicerub.ratio.core.theme.TextSecondary

@Composable
fun SectionHint(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextSecondary,
    align: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        textAlign = align,
        modifier = modifier.fillMaxWidth()
    )
}
