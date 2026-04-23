package es.virtualclubs.presentation.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring

// ── Durations ──────────────────────────────────────────────────────────────

object VCDuration {
    const val instant  : Int = 0
    const val xShort   : Int = 80
    const val short    : Int = 150
    const val medium   : Int = 250
    const val long     : Int = 400
    const val xLong    : Int = 600
}

// ── Easing curves ──────────────────────────────────────────────────────────

object VCEasing {
    /** Elementos que entran en pantalla */
    val enter       = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    /** Elementos que salen de pantalla */
    val exit        = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    /** Elementos que se mueven dentro de pantalla */
    val emphasized  = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    /** Transiciones estándar */
    val standard    = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    /** Feedback inmediato — botones, toggles */
    val linear      = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
}

// ── Tween specs ────────────────────────────────────────────────────────────

object VCTween {
    fun <T> enter(delay: Int = 0)  = tween<T>(VCDuration.medium, delay, VCEasing.enter)
    fun <T> exit(delay: Int = 0)   = tween<T>(VCDuration.short, delay, VCEasing.exit)
    fun <T> move(delay: Int = 0)   = tween<T>(VCDuration.long, delay, VCEasing.emphasized)
    fun <T> quick(delay: Int = 0)  = tween<T>(VCDuration.xShort, delay, VCEasing.linear)
}

// ── Spring specs ───────────────────────────────────────────────────────────

object VCSpring {
    /** Respuesta rápida y precisa — listas, chips */
    fun <T> snappy() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness    = Spring.StiffnessMediumLow
    )

    /** Suave sin rebote — diálogos, sheets */
    fun <T> smooth() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness    = Spring.StiffnessMediumLow
    )

    /** Rebote pronunciado — FAB, elementos de celebración */
    fun <T> bouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness    = Spring.StiffnessMedium
    )
}
