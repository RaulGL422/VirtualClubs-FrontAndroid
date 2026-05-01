package es.virtualclubs.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

private val _shapes = VCShapes()

val AppShapes = Shapes(
    extraSmall = _shapes.extraSmall,
    small      = _shapes.small,
    medium     = _shapes.medium,
    large      = _shapes.large,
    extraLarge = _shapes.extraLarge
)

data class VCShapes(
    val extraSmall : RoundedCornerShape = RoundedCornerShape(4.dp),
    val small      : RoundedCornerShape = RoundedCornerShape(6.dp),
    val medium     : RoundedCornerShape = RoundedCornerShape(10.dp),
    val large      : RoundedCornerShape = RoundedCornerShape(14.dp),
    val extraLarge : RoundedCornerShape = RoundedCornerShape(18.dp),
    val pill       : RoundedCornerShape = RoundedCornerShape(50.dp),
)
