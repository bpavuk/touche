package dev.bpavuk.touche.ui.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene

val forwardMovementSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(durationMillis = 220),
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { -it / 6 },
        animationSpec = tween(durationMillis = 220),
    )
}

val popMovementSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { -it / 6 },
        animationSpec = tween(durationMillis = 220),
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(durationMillis = 220),
    )
}

val predictivePopMovementSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(Int) -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { -it / 6 },
        animationSpec = tween(durationMillis = 220),
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(durationMillis = 220),
    )
}
