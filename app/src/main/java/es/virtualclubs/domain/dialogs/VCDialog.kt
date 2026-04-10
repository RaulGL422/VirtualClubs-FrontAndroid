@file:Suppress("unused")
package es.virtualclubs.domain.dialogs

/**
 * Movido a [es.virtualclubs.presentation.dialogs.VCDialog].
 * Este typealias existe solo para compatibilidad durante la migración.
 */
@Deprecated(
    message = "Usar es.virtualclubs.presentation.dialogs.VCDialog",
    replaceWith = ReplaceWith("VCDialog", "es.virtualclubs.presentation.dialogs.VCDialog")
)
typealias VCDialog = es.virtualclubs.presentation.dialogs.VCDialog
