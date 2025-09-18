package dev.bpavuk.touche.ui.surfaces

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import dev.bpavuk.touche.ui.preview.MultiDevicePreview
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min

data class Circle(
    val coordinates: Offset,
    val radius: Float,
    val color: Color,
    val timeSpawned: Long,
    val timeDied: Long
)

private fun List<Circle>.clusterCenter(): Offset? {
    if (isEmpty()) return null
    val x = sumOf { it.coordinates.x.toInt() } / size
    val y = sumOf { it.coordinates.y.toInt() } / size

    return Offset(x.toFloat(), y.toFloat())
}

private fun Offset.normalizeToArea(areaSize: Size): Offset {
    val x = if (x in 0f..areaSize.width) x else if (x <= 0f) 0f else areaSize.width
    val y = if (y in 0f..areaSize.height) y else if (y <= 0f) 0f else areaSize.height

    return Offset(x = x, y = y)
}

private fun randomOffset(areaSize: Size): Offset {
    val offsetX = (-areaSize.width.toInt()..areaSize.width.toInt()).random() / 2
    val offsetY = (-areaSize.height.toInt()..areaSize.height.toInt()).random() / 2

    Log.d("Cloudy", "random; X: $offsetX\tY: $offsetY")
    Log.d("Cloudy", "area: $areaSize")

    return Offset(
        x = offsetX.toFloat() + (areaSize.width / 2),
        y = offsetY.toFloat() + (areaSize.height / 2)
    )
}

private fun MutableList<Circle>.spawn(
    radius: Float,
    color: Color,
    timeSpawned: Long,
    lifetime: Int,
    areaSize: Size
) {
    val clusterCenter = clusterCenter() ?: randomOffset(areaSize)
    val areaCenter = (areaSize.width / 2) to (areaSize.height / 2)
    val positiveRange = (10..300)
    val negativeRange = (-300..-10)
    val xRange = if (clusterCenter.x > areaCenter.first) negativeRange else positiveRange
    val yRange = if (clusterCenter.y > areaCenter.second) negativeRange else positiveRange
    val offsetX = xRange.random()
    val offsetY = yRange.random()
    val offset = Offset(offsetX.toFloat(), offsetY.toFloat())

    Log.d("Cloudy", "offset: $offset")
    Log.d("Cloudy", "center: $clusterCenter")

    val positionX = clusterCenter.x + offsetX
    val positionY = clusterCenter.y + offsetY
    val coordinates = Offset(positionX, positionY).normalizeToArea(areaSize)

    val timeDied = timeSpawned + lifetime

    add(Circle(coordinates, radius, color, timeSpawned, timeDied))
}

private fun MutableList<Circle>.spawnRandom(color: Color, timeSpawned: Long, areaSize: Size) {
    Log.d("Cloudy", "area: $areaSize")
    val radius = (90..200).random().toFloat()
    val lifetime = (10_000..35_000).random()
    spawn(radius, color, timeSpawned, lifetime, areaSize)
}

@Composable
fun Cloudy(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    var frameTime: Long by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            frameTime = System.currentTimeMillis()
            delay(10)
        }
    }

    var area by remember { mutableStateOf(Size.Zero) }
    val circles = remember { mutableStateListOf<Circle>() }
    LaunchedEffect(area, circles) {
        val color = listOf(primaryColor, secondaryColor, tertiaryColor).random()
        circles.spawnRandom(color, frameTime, areaSize = area)
        while (true) {
            delay(100)
            while (circles.size < 50) {
                delay(100)
                val color = listOf(primaryColor, secondaryColor, tertiaryColor).random()
                circles.spawnRandom(color, frameTime, areaSize = area)
            }
            circles.toList().forEach { circle ->
                if (circle.timeDied < frameTime) {
                    circles.remove(circle)
                }
            }
        }
    }

    val animatedCircles = circles.map { circle ->
        val maxDeterminator = ((circle.timeDied - circle.timeSpawned) / 2).toFloat()
        val currentDeterminator = min(
            a = (circle.timeDied - frameTime).absoluteValue,
            b = (circle.timeSpawned - frameTime).absoluteValue
        )
        val normalizedDeterminator = (currentDeterminator / maxDeterminator) * PI
        val animatedRadiusMultiplier = ((1 + cos(normalizedDeterminator + PI)) / 2).toFloat()

        circle.copy(radius = animatedRadiusMultiplier * circle.radius)
    }
    Canvas(modifier.onGloballyPositioned {
        area = it.size.toSize()
    }) {
        animatedCircles.forEach { circle ->
            drawCircle(circle.color, circle.radius, center = circle.coordinates)
        }
    }
}

@MultiDevicePreview
@Preview
@Composable
private fun CloudyPreview() {
    ToucheTheme(darkTheme = true) {
        Cloudy(modifier = Modifier.fillMaxSize().background(Color.Black))
    }
}