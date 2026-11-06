package com.spicerub.ratio.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

@Composable
fun SpiceChip(
    label: String,
    tint: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showAddIcon: Boolean = false,
    stateLabel: String? = null
) {
    val shape = RoundedCornerShape(20.dp)
    val base = modifier
        .height(40.dp)
        .defaultMinSize(minWidth = 64.dp)
        .background(tint.copy(alpha = if (selected) 0.22f else 0.12f), shape)
        .border(if (selected) 2.dp else 1.dp, tint.copy(alpha = if (selected) 0.9f else 0.5f), shape)
        .clickable(onClick = onClick)
        .padding(horizontal = 14.dp)
    val withSemantics = if (stateLabel != null) {
        base.semantics { stateDescription = stateLabel }
    } else {
        base
    }

    Row(
        modifier = withSemantics,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showAddIcon) {
            Icon(
                imageVector = AppIcons.Add,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = tint
        )
    }
}
