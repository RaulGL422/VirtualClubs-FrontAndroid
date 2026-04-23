package es.virtualclubs.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object VCGradient {

    // ── Brand ──────────────────────────────────────────────────────────────

    /** Azul eléctrico de esquina a esquina — botones premium, hero cards */
    val brandBlue: Brush get() = Brush.linearGradient(
        colors = listOf(VCBlue700, VCBlue500),
        start  = Offset(0f, 0f),
        end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /** Naranja fuego — banners de acción, CTAs destacados */
    val brandOrange: Brush get() = Brush.linearGradient(
        colors = listOf(VCOrange700, VCOrange500),
        start  = Offset(0f, 0f),
        end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /** Azul → naranja — identidad Stadium, cabeceras de club */
    val brandStadium: Brush get() = Brush.linearGradient(
        colors = listOf(VCBlue700, VCOrange600),
        start  = Offset(0f, 0f),
        end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    // ── Surface overlays ───────────────────────────────────────────────────

    /** Overlay oscuro sobre imágenes — legibilidad de texto en cards con foto */
    val imageForeground: Brush get() = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color(0xCC070C18))
    )

    /** Overlay sutil para el fondo de pantallas con imagen de fondo */
    val screenOverlay: Brush get() = Brush.verticalGradient(
        colors = listOf(Color(0x80070C18), Color(0xF0070C18))
    )

    // ── Status ─────────────────────────────────────────────────────────────

    /** Verde activo — evento en vivo, estado online */
    val live: Brush get() = Brush.linearGradient(
        colors = listOf(VCGreen600, VCGreen400)
    )

    /** Azul suave — contenido próximo, pendiente */
    val upcoming: Brush get() = Brush.linearGradient(
        colors = listOf(VCBlue500, VCBlue300)
    )

    // ── Surfaces ───────────────────────────────────────────────────────────

    /** Fondo sutil azulado para cards en modo claro */
    val surfaceLight: Brush get() = Brush.verticalGradient(
        colors = listOf(VCNeutral50, VCNeutral100)
    )

    /** Fondo navy profundo para cards en modo oscuro */
    val surfaceDark: Brush get() = Brush.verticalGradient(
        colors = listOf(VCNeutral900, VCNeutral850)
    )
}
