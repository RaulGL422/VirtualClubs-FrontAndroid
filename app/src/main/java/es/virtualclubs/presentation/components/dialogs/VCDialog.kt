package es.virtualclubs.presentation.components.dialogs

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

/**
 * Contrato para todos los diálogos del sistema.
 *
 * Cada diálogo implementa esta interfaz para que [GlobalUIManager] pueda
 * mostrarlo sin conocer su implementación concreta. Las propiedades tienen
 * valores por defecto para minimizar el boilerplate en cada implementación.
 */
interface VCDialog {
    /** ID del recurso de string para el título. `null` si no hay título. */
    val titleRes: Int? get() = null
    /** Si el diálogo puede descartarse tocando fuera o con el botón Atrás. */
    val dismissible: Boolean get() = true
    /** Si el diálogo bloquea la UI de fondo (sin botón de cierre automático). */
    val blockDialog: Boolean get() = false
    /** ID del recurso de string para el botón de confirmación. `null` oculta el botón. */
    val confirmTextRes: Int? get() = null
    /** ID del recurso de string para el botón de cancelar. `null` oculta el botón. */
    val dismissTextRes: Int? get() = null
    /** Callback al pulsar confirmar. `null` oculta el botón de confirmación. */
    val onConfirm: (() -> Unit)? get() = null

    /** Contenido principal del diálogo renderizado en un [ColumnScope]. */
    @Composable
    fun ColumnScope.Content()
}
