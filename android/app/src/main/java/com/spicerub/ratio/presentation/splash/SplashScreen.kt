package com.spicerub.ratio.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
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
import com.spicerub.ratio.core.theme.Paprika
import com.spicerub.ratio.core.theme.RoastedBottom
import com.spicerub.ratio.core.theme.SplashOverlayBottom
import com.spicerub.ratio.core.theme.SplashOverlayTop
import com.spicerub.ratio.core.theme.TurmericCream

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    SplashScreen(finished = finished, onFinished = onFinished)
}

@Composable
fun SplashScreen(
    finished: Boolean,
    onFinished: () -> Unit
) {
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    val loadingDescription = stringResource(R.string.cd_loading)
    val transition = rememberInfiniteTransition(label = "splash")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(SplashOverlayTop, SplashOverlayBottom))
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
                enter = fadeIn(tween(600)) + scaleIn(
                    initialScale = 0.82f,
                    animationSpec = spring(dampingRatio = 0.62f, stiffness = 220f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(
                        modifier = Modifier
                            .size(206.dp)
                            .graphicsLayer { rotationZ = spin }
                    ) {
                        val stroke = 3.dp.toPx()
                        drawArc(
                            color = Cardamom.copy(alpha = 0.35f),
                            startAngle = 0f,
                            sweepAngle = 240f,
                            useCenter = false,
                            topLeft = Offset(stroke, stroke),
                            size = Size(size.width - stroke * 2f, size.height - stroke * 2f),
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color(0x33F2D9A6),
                        border = BorderStroke(2.dp, GoldRim.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .size(176.dp)
                            .graphicsLayer {
                                scaleX = breathe
                                scaleY = breathe
                            }
                            .shadow(18.dp, CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(132.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            SpiceDust(transition = transition)
            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = RoastedBottom,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                )
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = TurmericCream,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.tagline),
                style = MaterialTheme.typography.labelLarge,
                color = GoldRim.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = loadingDescription
                }
            ) {
                LinearProgressIndicator(
                    color = Paprika,
                    trackColor = Cardamom.copy(alpha = 0.25f),
                    modifier = Modifier
                        .width(168.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.loading),
                    style = MaterialTheme.typography.labelSmall,
                    color = TurmericCream.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
private fun SpiceDust(transition: InfiniteTransition) {
    val colors = listOf(
        Color(0xFFE0A526),
        Color(0xFFC34524),
        Color(0xFF6F9E57),
        Color(0xFFE0A526),
        Color(0xFFC34524),
        Color(0xFF6F9E57)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom
    ) {
        for (index in colors.indices) {
            val drift by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2200,
                        delayMillis = index * 400,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "dust"
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .graphicsLayer {
                        translationY = -26.dp.toPx() * drift
                        alpha = if (drift < 0.5f) drift * 2f * 0.5f else (1f - drift) * 2f * 0.5f
                    }
                    .background(colors[index], CircleShape)
            )
        }
    }
}
