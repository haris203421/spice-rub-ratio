package com.spicerub.ratio.presentation.dials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spicerub.ratio.R
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.di.AppViewModelFactory
import com.spicerub.ratio.core.theme.Cardamom
import com.spicerub.ratio.core.theme.DialsOverlayBottom
import com.spicerub.ratio.core.theme.DialsOverlayTop
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.TextPrimary
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.core.ui.AppIcons
import com.spicerub.ratio.core.ui.AppBackground
import com.spicerub.ratio.core.ui.PrimaryButton
import com.spicerub.ratio.core.ui.RatioStrip
import com.spicerub.ratio.core.ui.RotaryDial
import com.spicerub.ratio.core.ui.SectionHint

@Composable
fun DialsRoute(onBack: () -> Unit) {
    val viewModel: DialsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    DialsScreen(
        state = state,
        onHeatChange = viewModel::setHeat,
        onSaltChange = viewModel::setSalt,
        onBack = onBack
    )
}

@Composable
fun DialsScreen(
    state: DialsUiState,
    onHeatChange: (Double) -> Unit,
    onSaltChange: (Double) -> Unit,
    onBack: () -> Unit
) {
    AppBackground(overlayTop = DialsOverlayTop, overlayBottom = DialsOverlayBottom) {
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
                text = stringResource(R.string.heat_and_salt),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            DialBlock(
                caption = stringResource(R.string.dial_heat),
                value = state.heatValue,
                word = stringResource(
                    when (state.heatWord) {
                        HeatWord.MILD -> R.string.heat_word_mild
                        HeatWord.MEDIUM -> R.string.heat_word_medium
                        HeatWord.HOT -> R.string.heat_word_hot
                        HeatWord.FIERY -> R.string.heat_word_fiery
                    }
                ),
                tint = Paprika
            ) {
                RotaryDial(
                    value = state.heat,
                    minValue = GameConfig.HEAT_MIN,
                    maxValue = GameConfig.HEAT_MAX,
                    step = GameConfig.HEAT_STEP,
                    tint = Paprika,
                    icon = painterResource(R.drawable.sprite_1),
                    iconDescription = stringResource(R.string.cd_heat_dial),
                    iconSize = (28 + 26 * state.heatIconScale).dp,
                    stateLabel = stringResource(R.string.state_heat, state.heatValue),
                    onValueChange = onHeatChange,
                    diameter = 170.dp
                )
            }

            DialBlock(
                caption = stringResource(R.string.dial_salt),
                value = state.saltValue,
                word = stringResource(
                    when (state.saltWord) {
                        SaltWord.LOW -> R.string.salt_word_low
                        SaltWord.BALANCED -> R.string.salt_word_balanced
                        SaltWord.SALTY -> R.string.salt_word_salty
                    }
                ),
                tint = Cardamom
            ) {
                RotaryDial(
                    value = state.salt,
                    minValue = GameConfig.SALT_MIN,
                    maxValue = GameConfig.SALT_MAX,
                    step = GameConfig.SALT_STEP,
                    tint = Cardamom,
                    icon = painterResource(R.drawable.sprite_4),
                    iconDescription = stringResource(R.string.cd_salt_dial),
                    iconSize = (28 + 26 * state.saltIconScale).dp,
                    stateLabel = stringResource(R.string.state_salt, state.saltValue),
                    onValueChange = onSaltChange,
                    diameter = 170.dp
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            SectionHint(
                text = stringResource(R.string.hint_dials_preview),
                align = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            RatioStrip(
                segments = state.segments,
                height = 16.dp,
                corner = 8.dp,
                showRim = true
            )
            Spacer(modifier = Modifier.height(14.dp))
            PrimaryButton(
                text = stringResource(R.string.apply_dials),
                icon = AppIcons.Check,
                height = 56.dp,
                onClick = onBack
            )
        }
    }
}

@Composable
private fun DialBlock(
    caption: String,
    value: String,
    word: String,
    tint: Color,
    dial: @Composable () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = caption,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(contentAlignment = Alignment.Center) {
            dial()
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = tint
        )
        Text(
            text = word,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
    }
}
