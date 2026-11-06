package com.spicerub.ratio.presentation.pantry

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spicerub.ratio.R
import com.spicerub.ratio.core.di.AppViewModelFactory
import com.spicerub.ratio.core.theme.Cardamom
import com.spicerub.ratio.core.theme.GoldRim
import com.spicerub.ratio.core.theme.MenuOverlayBottom
import com.spicerub.ratio.core.theme.MenuOverlayTop
import com.spicerub.ratio.core.theme.SurfaceCard
import com.spicerub.ratio.core.theme.TextMuted
import com.spicerub.ratio.core.theme.TextPrimary
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.core.ui.AppIcons
import com.spicerub.ratio.core.ui.AppBackground
import com.spicerub.ratio.core.ui.SectionHint
import com.spicerub.ratio.core.ui.SpiceChip
import com.spicerub.ratio.domain.model.SpiceForm

@Composable
fun PantryRoute(onBack: () -> Unit) {
    val viewModel: PantryViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PantryEvent.SpiceAdded -> onBack()
            }
        }
    }

    PantryScreen(
        state = state,
        onBack = onBack,
        onFilterChange = viewModel::setFilter,
        onToggleOnHand = viewModel::toggleOnHand,
        onPick = viewModel::addToBlend
    )
}

@Composable
fun PantryScreen(
    state: PantryUiState,
    onBack: () -> Unit,
    onFilterChange: (PantryFilter) -> Unit,
    onToggleOnHand: (String) -> Unit,
    onPick: (String) -> Unit
) {
    AppBackground(overlayTop = MenuOverlayTop, overlayBottom = MenuOverlayBottom) {
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
                    contentDescription = stringResource(R.string.cd_back),
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.spice_pantry),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
            )
            Box(
                modifier = Modifier
                    .background(Cardamom.copy(alpha = 0.10f), RoundedCornerShape(12.dp))
                    .border(1.dp, Cardamom.copy(alpha = 0.33f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.pantry_count, state.totalCount),
                    style = MaterialTheme.typography.labelMedium,
                    color = Cardamom
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpiceChip(
                label = stringResource(R.string.filter_all),
                tint = Cardamom,
                selected = state.filter == PantryFilter.ALL,
                onClick = { onFilterChange(PantryFilter.ALL) }
            )
            SpiceChip(
                label = stringResource(R.string.filter_on_hand),
                tint = GoldRim,
                selected = state.filter == PantryFilter.ON_HAND,
                onClick = { onFilterChange(PantryFilter.ON_HAND) }
            )
            SpiceChip(
                label = stringResource(R.string.filter_heat),
                tint = Color(0xFFC34524),
                selected = state.filter == PantryFilter.HEAT,
                onClick = { onFilterChange(PantryFilter.HEAT) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        SectionHint(
            text = stringResource(R.string.hint_pantry),
            align = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.isEmpty) {
                EmptyFilter()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.rows, key = { it.spiceId }) { row ->
                        PantryRow(
                            row = row,
                            onToggleOnHand = { onToggleOnHand(row.spiceId) },
                            onPick = { onPick(row.spiceId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PantryRow(
    row: PantryRowUi,
    onToggleOnHand: () -> Unit,
    onPick: () -> Unit
) {
    val tint = Color(row.colorHex)
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(tint, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = row.initial,
                    style = MaterialTheme.typography.labelLarge,
                    color = SurfaceCard
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = row.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(
                        when (row.form) {
                            SpiceForm.GROUND -> R.string.form_ground
                            SpiceForm.SEED -> R.string.form_seed
                            SpiceForm.FLAKE -> R.string.form_flake
                        }
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (row.inBlend) tint else TextMuted
                )
            }
            Text(
                text = stringResource(R.string.density_per_tsp, row.densityLabel),
                style = MaterialTheme.typography.labelMedium,
                color = Cardamom
            )
            IconToggleButton(
                checked = row.onHand,
                onCheckedChange = { onToggleOnHand() },
                modifier = Modifier
                    .size(48.dp)
                    .semantics {
                        stateDescription = ""
                    }
            ) {
                Icon(
                    imageVector = if (row.onHand) AppIcons.Favorite else AppIcons.FavoriteBorder,
                    contentDescription = stringResource(R.string.cd_on_hand, row.name),
                    tint = if (row.onHand) GoldRim else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyFilter() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_3),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.empty_filter),
            style = MaterialTheme.typography.titleSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
