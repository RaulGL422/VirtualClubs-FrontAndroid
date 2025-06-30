package es.virtualclubs.presentation.handlers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.text.clear

object NotifyHandler {
    private val notifyMessages = mapOf(
        TypeMessage.EMAIL_CODE_SENT to R.string.email_code_sent,
        TypeMessage.JOINED_PUBLIC_CLASS to R.string.joined_public_class,
        TypeMessage.REQUEST_ACCEPTED to R.string.request_accepted,
        TypeMessage.REQUEST_DECLINED to R.string.request_declined,
        TypeMessage.ENROLLMENT_REQUEST_ACCEPTED to R.string.enrollment_request_accepted,
        TypeMessage.ENROLLMENT_REQUEST_DECLINED to R.string.enrollment_request_declined,
        TypeMessage.REQUEST_SENT to R.string.request_sent,
        TypeMessage.REMOVED_INSCRIPTION to R.string.removed_inscription,
        TypeMessage.ENROLLMENT_REQUEST_SENT to R.string.enrollment_request_sent,
        TypeMessage.ASSISTANCE_ADDED to R.string.assistance_added,
        TypeMessage.REMOVED_ENROLLMENT to R.string.enrollment_removed,
        TypeMessage.CLASS_CREATED to R.string.class_created,
        TypeMessage.CLASS_UPDATED to R.string.class_updated,
        TypeMessage.CLASS_DELETED to R.string.class_deleted,
        TypeMessage.CREATE_EMPLOYEE_ROLE_SUCCESS to R.string.create_employee_role_success,
        TypeMessage.DELETE_EMPLOYEE_ROLE_SUCCESS to R.string.delete_employee_role_success,
        TypeMessage.UPDATE_EMPLOYEE_ROLE_SUCCESS to R.string.update_employee_role_success,
        TypeMessage.CREATE_EMPLOYEE_SUCCESS to R.string.create_employee_success,
        TypeMessage.DELETE_EMPLOYEE_SUCCESS to R.string.delete_employee_success,
        TypeMessage.UPDATE_EMPLOYEE_SUCCESS to R.string.update_employee_success,
        TypeMessage.CREATED_ROOM to R.string.created_room,
        TypeMessage.DELETED_ROOM to R.string.deleted_room,
        TypeMessage.UPDATED_ROOM to R.string.updated_room,
    )

    var notifyMessage: Int? by mutableStateOf(null)

    fun showNotify(type: TypeMessage) {
        val resId = notifyMessages[type] ?: return
        showNotify(resId)
    }

    fun showNotify(idMessage: Int) {
        notifyMessage = idMessage
        ErrorHandler.clear()
    }

    fun clear() {
        notifyMessage = null
    }
}