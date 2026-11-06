package com.spicerub.ratio.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
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
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.SurfaceCard
import com.spicerub.ratio.core.theme.TextPrimary
import com.spicerub.ratio.core.theme.TextSecondary
import com.spicerub.ratio.core.ui.AppIcons
import com.spicerub.ratio.core.ui.AppBackground
import com.spicerub.ratio.core.ui.PrimaryButton
import com.spicerub.ratio.core.ui.RatioStrip
import com.spicerub.ratio.core.ui.SecondaryButton
import com.spicerub.ratio.core.ui.SectionHint
import com.spicerub.ratio.core.ui.StatRow

@Composable
fun MenuRoute(
    onOpenBuilder: (String) -> Unit,
    onOpenDials: () -> Unit,
    onOpenPantry: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MenuEvent.OpenBuilder -> onOpenBuilder(event.blendId)
            }
        }
    }

    MenuScreen(
        state = state,
        onStartBlend = viewModel::startNewBlend,
        onOpenBlend = viewModel::openBlend,
        onOpenDials = onOpenDials,
        onOpenPantry = onOpenPantry
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onStartBlend: () -> Unit,
    onOpenBlend: (String) -> Unit,
    onOpenDials: () -> Unit,
    onOpenPantry: () -> Unit
) {
    AppBackground(overlayTop = MenuOverlayTop, overlayBottom = MenuOverlayBottom) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.padding(start = 10.dp)
            )
            Box(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onOpenPantry,
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_open_pantry),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            AnimatedVisibility(
                visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
                enter = fadeIn(tween(320)) + scaleIn(initialScale = 0.94f, animationSpec = tween(320))
            ) {
                PrimaryButton(
                    text = stringResource(R.string.start_blend),
                    subtitle = stringResource(R.string.start_blend_hint),
                    icon = AppIcons.Add,
                    onClick = onStartBlend,
                    height = 118.dp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.showStats) {
                StatRow(
                    stats = listOf(
                        Triple(
                            stringResource(R.string.stat_blends),
                            state.blendCount.toString(),
                            Paprika
                        ),
                        Triple(
                            stringResource(R.string.stat_spices),
                            state.spiceCount.toString(),
                            Cardamom
                        )
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
                SectionHint(
                    text = stringResource(R.string.hint_open_blend),
                    align = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.isEmpty) {
                EmptyCabinet()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = state.blends, key = { it.id }) { blend ->
                        BlendCard(blend = blend, onClick = { onOpenBlend(blend.id) })
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.heat_and_salt),
                onClick = onOpenDials,
                height = 48.dp,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.spice_pantry),
                onClick = onOpenPantry,
                height = 48.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BlendCard(
    blend: BlendCardUi,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        enter = fadeIn(tween(280, easing = FastOutSlowInEasing)) +
            slideInVertically(tween(280)) { 24 }
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(1.dp, GoldRim.copy(alpha = 0.25f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .semantics(mergeDescendants = true) { contentDescription = blend.description }
        ) {
            Row(modifier = Modifier.height(96.dp)) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(Color(blend.spineColorHex))
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = blend.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = blend.profile,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    RatioStrip(segments = blend.segments, height = 8.dp)
                }
                Box(
                    modifier = Modifier
                        .padding(end = 14.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCabinet() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(18.dp))
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.empty_cabinet),
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.empty_cabinet_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
