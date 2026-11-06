package com.spicerub.ratio.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spicerub.ratio.R
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.di.AppViewModelFactory
import com.spicerub.ratio.core.theme.BuilderOverlayBottom
import com.spicerub.ratio.core.theme.BuilderOverlayTop
import com.spicerub.ratio.core.theme.Cardamom
import com.spicerub.ratio.core.theme.GoldRim
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.SurfaceCard
import com.spicerub.ratio.core.theme.SurfaceMuted
import com.spicerub.ratio.core.theme.TextOnPrimary
import com.spicerub.ratio.core.theme.TextPrimary
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.core.ui.AppIcons
import com.spicerub.ratio.core.ui.AppBackground
import com.spicerub.ratio.core.ui.GlassPanel
import com.spicerub.ratio.core.ui.PrimaryButton
import com.spicerub.ratio.core.ui.RatioDonut
import com.spicerub.ratio.core.ui.RatioStrip
import com.spicerub.ratio.core.ui.SectionHint
import com.spicerub.ratio.core.ui.SpiceChip
import com.spicerub.ratio.core.ui.SpiceSliderRow
import com.spicerub.ratio.domain.model.MeasureUnit

@Composable
fun GameRoute(
    onBack: () -> Unit,
    onOpenDials: () -> Unit,
    onOpenPantry: () -> Unit,
    onScaled: (String, Int) -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Scaled -> onScaled(event.blendId, event.batchGrams)
            }
        }
    }

    GameScreen(
        state = state,
        onBack = onBack,
        onOpenDials = onOpenDials,
        onOpenPantry = onOpenPantry,
        onPartsChange = viewModel::setParts,
        onRemoveSpice = viewModel::removeSpice,
        onAddSpice = viewModel::addSpice,
        onBatchChange = viewModel::setBatch,
        onUnitChange = viewModel::setUnit,
        onScale = viewModel::scaleAndLabel
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onBack: () -> Unit,
    onOpenDials: () -> Unit,
    onOpenPantry: () -> Unit,
    onPartsChange: (String, Double) -> Unit,
    onRemoveSpice: (String) -> Unit,
    onAddSpice: (String) -> Unit,
    onBatchChange: (Int) -> Unit,
    onUnitChange: (MeasureUnit) -> Unit,
    onScale: () -> Unit
) {
    AppBackground(overlayTop = BuilderOverlayTop, overlayBottom = BuilderOverlayBottom) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back_to_cabinet),
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
            )
            Box(
                modifier = Modifier
                    .background(Cardamom.copy(alpha = 0.10f), RoundedCornerShape(12.dp))
                    .border(1.dp, Cardamom.copy(alpha = 0.33f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            ) {
                Text(
                    text = state.totalPartsValue + " " + stringResource(R.string.label_total_parts),
                    style = MaterialTheme.typography.labelMedium,
                    color = Cardamom
                )
            }
            IconButton(onClick = onOpenPantry, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_open_pantry),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                RatioDonut(
                    segments = state.segments,
                    diameter = 184.dp,
                    strokeWidth = 38.dp,
                    centerValue = state.yieldValue,
                    centerLabel = stringResource(R.string.grams_total),
                    description = state.donutDescription
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricLabel(
                    caption = stringResource(R.string.group_heat),
                    value = state.heatValue,
                    tint = Paprika
                )
                MetricLabel(
                    caption = stringResource(R.string.group_salt),
                    value = state.saltValue,
                    tint = Cardamom
                )
                MetricLabel(
                    caption = stringResource(R.string.label_yield),
                    value = state.yieldValue,
                    tint = GoldRim
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            RatioStrip(
                segments = state.segments,
                height = 14.dp,
                corner = 7.dp,
                showRim = true,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = state.rows, key = { it.spiceId }) { row ->
                    SpiceSliderRow(
                        name = row.name,
                        partsLabel = row.partsLabel,
                        parts = row.parts.toFloat(),
                        tint = Color(row.colorHex),
                        badge = when (row.badge) {
                            GroupBadge.HEAT -> stringResource(R.string.group_heat)
                            GroupBadge.SALT -> stringResource(R.string.group_salt)
                            null -> null
                        },
                        badgeTint = if (row.badge == GroupBadge.SALT) Cardamom else Paprika,
                        removeLabel = stringResource(R.string.cd_remove_spice, row.name),
                        stateLabel = stringResource(R.string.state_parts, row.partsLabel),
                        onPartsChange = { parts -> onPartsChange(row.spiceId, parts) },
                        onRemove = { onRemoveSpice(row.spiceId) }
                    )
                }
            }

            if (state.available.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.available, key = { it.spiceId }) { chip ->
                        SpiceChip(
                            label = chip.name,
                            tint = Color(chip.colorHex),
                            selected = false,
                            showAddIcon = true,
                            onClick = { onAddSpice(chip.spiceId) }
                        )
                    }
                }
            }
        }

        GlassPanel {
            SectionHint(text = stringResource(R.string.batch_target))
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = state.presets, key = { it.grams.toString() + it.isMeat }) { preset ->
                    SpiceChip(
                        label = if (preset.isMeat) {
                            stringResource(R.string.batch_meat)
                        } else {
                            stringResource(R.string.batch_chip, preset.grams)
                        },
                        tint = Paprika,
                        selected = state.batchGrams == preset.grams,
                        onClick = { onBatchChange(preset.grams) }
                    )
                }
            }
            Slider(
                value = state.batchGrams.toFloat(),
                onValueChange = { value -> onBatchChange(value.toInt()) },
                valueRange = GameConfig.BATCH_MIN_G.toFloat()..GameConfig.BATCH_MAX_G.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Paprika,
                    activeTrackColor = Paprika,
                    inactiveTrackColor = SurfaceMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            )
            UnitToggle(unit = state.unit, onUnitChange = onUnitChange)
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = onOpenDials,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.heat_and_salt),
                    style = MaterialTheme.typography.labelLarge,
                    color = Cardamom
                )
            }
            if (!state.canScale) {
                SectionHint(
                    text = stringResource(R.string.hint_need_spices),
                    color = Paprika,
                    align = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            PrimaryButton(
                text = stringResource(R.string.scale_and_label),
                icon = AppIcons.Check,
                enabled = state.canScale,
                disabledHint = stringResource(R.string.hint_need_spices),
                onClick = onScale
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun MetricLabel(
    caption: String,
    value: String,
    tint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = tint
        )
    }
}

@Composable
private fun UnitToggle(
    unit: MeasureUnit,
    onUnitChange: (MeasureUnit) -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, Cardamom.copy(alpha = 0.35f), shape)
    ) {
        UnitSegment(
            label = stringResource(R.string.label_grams),
            selected = unit == MeasureUnit.GRAMS,
            onClick = { onUnitChange(MeasureUnit.GRAMS) },
            modifier = Modifier.weight(1f)
        )
        UnitSegment(
            label = stringResource(R.string.label_spoons),
            selected = unit == MeasureUnit.SPOONS,
            onClick = { onUnitChange(MeasureUnit.SPOONS) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun UnitSegment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (selected) Cardamom else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) TextOnPrimary else TextSecondary
        )
    }
}
