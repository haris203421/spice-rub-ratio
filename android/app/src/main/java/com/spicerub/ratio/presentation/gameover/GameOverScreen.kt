package com.spicerub.ratio.presentation.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.spicerub.ratio.core.di.AppViewModelFactory
import com.spicerub.ratio.core.theme.Cardamom
import com.spicerub.ratio.core.theme.GoldRim
import com.spicerub.ratio.core.theme.LabelOverlayBottom
import com.spicerub.ratio.core.theme.LabelOverlayTop
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.TextPrimary
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.core.ui.AppIcons
import com.spicerub.ratio.core.ui.AppBackground
import com.spicerub.ratio.core.ui.GlassPanel
import com.spicerub.ratio.core.ui.PrimaryButton
import com.spicerub.ratio.core.ui.RatioDonut
import com.spicerub.ratio.core.ui.RatioStrip
import com.spicerub.ratio.core.ui.SecondaryButton
import com.spicerub.ratio.core.ui.SectionHint
import com.spicerub.ratio.core.ui.StatRow

@Composable
fun GameOverRoute(
    blendId: String,
    batchGrams: Int,
    onBlendAgain: (String) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(blendId, batchGrams) {
        viewModel.load(blendId, batchGrams)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameOverEvent.OpenBuilder -> onBlendAgain(event.blendId)
            }
        }
    }

    GameOverScreen(
        state = state,
        onBlendAgain = viewModel::blendAgain,
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    onBlendAgain: () -> Unit,
    onMenu: () -> Unit
) {
    AppBackground(overlayTop = LabelOverlayTop, overlayBottom = LabelOverlayBottom) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_label_seal),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
            Text(
                text = stringResource(R.string.label_ready),
                style = MaterialTheme.typography.titleLarge,
                color = Paprika,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
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
                    diameter = 172.dp,
                    strokeWidth = 34.dp,
                    centerValue = state.batchLabel,
                    centerLabel = stringResource(R.string.grams_total),
                    description = state.donutDescription
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.blendName,
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            SectionHint(
                text = state.profile,
                color = Cardamom,
                align = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            RatioStrip(
                segments = state.segments,
                height = 14.dp,
                corner = 7.dp,
                showRim = true,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (state.showStats) {
                StatRow(
                    stats = listOf(
                        Triple(stringResource(R.string.label_batch), state.batchLabel, Paprika),
                        Triple(stringResource(R.string.stat_spices), state.spiceCountLabel, Cardamom),
                        Triple(stringResource(R.string.label_total_parts), state.partsLabel, GoldRim)
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            SectionHint(
                text = stringResource(R.string.label_recipe),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = state.lines, key = { it.spiceId }) { line ->
                    LabelLine(line = line)
                }
            }
        }

        GlassPanel {
            if (state.shelfDays > 0) {
                SectionHint(
                    text = stringResource(R.string.best_before, state.shelfDays),
                    color = GoldRim,
                    align = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            SectionHint(
                text = stringResource(R.string.label_jar_hint),
                align = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.blend_again),
                icon = AppIcons.Refresh,
                enabled = state.isReady,
                height = 60.dp,
                onClick = onBlendAgain
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.back_to_menu),
                onClick = onMenu
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun LabelLine(line: LabelLineUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color(line.colorHex), CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = line.name,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = line.amount,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
    }
}
