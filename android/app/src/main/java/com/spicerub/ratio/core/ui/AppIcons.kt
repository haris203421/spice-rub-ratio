package com.spicerub.ratio.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.spicerub.ratio.R

object AppIcons {
    val Add: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_add)

    val Close: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_close)

    val ArrowBack: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_arrow_back)

    val Check: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_check)

    val Refresh: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_refresh)

    val Favorite: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_favorite)

    val FavoriteBorder: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_favorite_border)
}
