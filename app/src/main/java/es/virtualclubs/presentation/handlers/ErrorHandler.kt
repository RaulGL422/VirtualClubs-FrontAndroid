package es.iesfernandoaguilar.models.objects

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.iesfernandoaguilar.R
import es.iesfernandoaguilar.models.enums.TypeMessage

object ErrorHandler {
    private val errorMessages = mapOf(
        TypeMessage.REGISTER to R.string.register_error,
        TypeMessage.LOGIN to R.string.login_error,
        TypeMessage.EMAIL_NOT_VALID to R.string.emailNotValid,
        TypeMessage.EMAIL_YET_REGISTERED to R.string.email_yet_registered,
        TypeMessage.USER_NOT_VALID to R.string.userNotValid,
        TypeMessage.RECEIVE_PASSWORD_ERROR to R.string.recieve_password_error,
        TypeMessage.INCORRECT_CREDENTIALS to R.string.incorrectCredentials,
        TypeMessage.COMPLETE_REGISTER_ERROR to R.string.complete_register_error,
        TypeMessage.USER_NOT_FOUND to R.string.user_not_found,
        TypeMessage.EMAIL_NOT_FOUND to R.string.emailNotFound,
        TypeMessage.INSCRIPTION_NOT_FOUND to R.string.inscription_not_found,
        TypeMessage.ENROLLMENT_REQUEST_NOT_FOUND to R.string.enrollment_request_not_found,
        TypeMessage.NO_CLUB_PERMISSION to R.string.no_club_permission,
        TypeMessage.CLASS_ENROLLMENT_NOT_FOUND to R.string.class_enrollment_not_found,
        TypeMessage.CLASS_NOT_FOUND to R.string.class_not_found,
        TypeMessage.EMAIL_CODE_ERROR to R.string.emailCodeError,
        TypeMessage.REQUEST_ACTIVE_CODE_ERROR to R.string.request_active_code_error,
        TypeMessage.VERIFY_ACTIVE_CODE_ERROR to R.string.verify_active_code_error,
        TypeMessage.USER_CODE_IS_WRONG to R.string.userCodeIsWrong,
        TypeMessage.UPDATE_FAILED to R.string.updateFailed,
        TypeMessage.INVALID_NUMBER_PHONE  to R.string.invalid_number_phone,
        TypeMessage.INVALID_DATE to R.string.invalid_date,
        TypeMessage.REQUEST_ENROLLMENT_REQUEST_FAILED to R.string.enrollment_request_failed,
        TypeMessage.REQUEST_ENROLLMENT_CLASSES_FAILED to R.string.enrollment_classes_failed,
        TypeMessage.REQUEST_PUBLIC_CLASSES_FAILED to R.string.public_classes_failed,
        TypeMessage.USER_CANT_ENROLL_BY_AGE to R.string.cant_enroll_age,
        TypeMessage.CLASS_CAPACITY_IS_FULL to R.string.cant_enroll_capacity,
        TypeMessage.JOIN_PUBLIC_CLASS_FAILED to R.string.join_public_class_failed,
        TypeMessage.CANT_REMOVE_ENROLLMENT_REQUEST to R.string.cant_remove_request,
        TypeMessage.ACCEPT_ENROLLMENT_REQUEST_FAILED to R.string.cant_accept_request,
        TypeMessage.DECLINE_ENROLLMENT_REQUEST_FAILED to R.string.cant_decline_request,
        TypeMessage.RESOLVE_REQUEST_FAILED to R.string.cant_complete_request,
        TypeMessage.REQUEST_USER_REQUEST_FAILED to R.string.request_user_request_failed,
        TypeMessage.CANT_CREATE_INSCRIPTION to R.string.cant_create_inscription,
        TypeMessage.CANT_DELETE_INSCRIPTION_REQUEST to R.string.cant_delete_inscription_request,
        TypeMessage.INSCRIPTION_YET_EXIST to R.string.inscription_yet_exist,
        TypeMessage.REQUEST_CLUB_USER_RESUME_FAILED to R.string.request_clubs_resume_failed,
        TypeMessage.REQUEST_CLUB_DATA_FAILED to R.string.request_club_data_failed,
        TypeMessage.CANT_CREATE_REQUEST to R.string.cant_create_request,
        TypeMessage.CANT_DELETE_INSCRIPTION to R.string.cant_delete_inscription,
        TypeMessage.DELETE_INSCRIPTION_FAILED to R.string.cant_delete_inscription,
        TypeMessage.CANT_INVITE_OWNER to R.string.cant_invite_owner,
        TypeMessage.CREATE_ENROLLMENT_REQUEST_FAILED to R.string.create_enrollment_request_failed,
        TypeMessage.CANT_CREATE_ENROLLMENT_REQUEST to R.string.cant_create_enrollment_request,
        TypeMessage.CLASS_ENROLLMENT_YET_EXIST to R.string.class_enrollment_yet_exist,
        TypeMessage.CANT_INVITE_INSTRUCTOR to R.string.cant_invite_instructor,
        TypeMessage.REQUEST_CLASS_INFO_FAILED to R.string.request_class_info_failed,
        TypeMessage.REMOVE_CLASS_ENROLLMENT_FAILED to R.string.remove_class_enrollment_failed,
        TypeMessage.CANT_REMOVE_ENROLLMENT to R.string.cant_remove_enrollment,
        TypeMessage.REQUEST_STUDENTS_CAN_JOIN_FAILED to R.string.request_students_can_join_failed,
        TypeMessage.ADD_NEW_ASSISTANCE_FAILED to R.string.add_new_assistance_failed,
        TypeMessage.CANT_CREATE_ASSISTANCE to R.string.cant_create_assistance,
        TypeMessage.REQUEST_ASSISTANCE_FAILED to R.string.request_assistance_failed,
        TypeMessage.CLUB_NOT_FOUND to R.string.club_not_found,
        TypeMessage.EMPLOYEE_NOT_FOUND to R.string.employee_not_found,
        TypeMessage.REQUEST_NOT_FOUND to R.string.request_not_found,
        TypeMessage.NOT_EMPLOYEE_ROLE_FOUNDED to R.string.not_employee_role_founded,
        TypeMessage.ROOM_NOT_FOUNDED to R.string.room_not_founded,
        TypeMessage.REQUEST_ROOMS_FAILED to R.string.request_rooms_failed,
        TypeMessage.REQUEST_STUDENT_FAILED to R.string.request_student_failed,
        TypeMessage.REQUEST_EMPLOYEES_FAILED to R.string.request_employees_failed,
        TypeMessage.REQUEST_EMPLOYEE_ROLES_FAILED to R.string.request_employee_roles_failed,
        TypeMessage.REQUEST_CLASSES_FAILED to R.string.request_classes_failed,
        TypeMessage.CREATE_NEW_ROOM_FAILED to R.string.create_new_room_failed,
        TypeMessage.UPDATE_ROOM_FAILED to R.string.update_room_failed,
        TypeMessage.REMOVE_ROOM_FAILED to R.string.remove_room_failed,
        TypeMessage.CANT_CREATE_ROOM_FAILED to R.string.cant_create_room_failed,
        TypeMessage.CANT_UPDATE_ROOM_FAILED to R.string.cant_update_room_failed,
        TypeMessage.CANT_REMOVE_ROOM_FAILED to R.string.cant_remove_room_failed,
        TypeMessage.NO_CAPACITY_ROOM to R.string.no_capacity_room,
        TypeMessage.CLUB_CREATED_FAILED to R.string.club_created_failed,
        TypeMessage.CANT_CREATE_CLUB to R.string.cant_create_club,
        TypeMessage.INVALID_CIF to R.string.invalid_cif,
        TypeMessage.PHOTO_CANT_BE_UPLOADED to R.string.photo_cant_be_uploaded,
        TypeMessage.CANT_CREATE_EMPLOYEE_ROLE to R.string.cant_create_employee_role,
        TypeMessage.CANT_DELETE_EMPLOYEE_ROLE to R.string.cant_delete_employee_role,
        TypeMessage.CREATE_EMPLOYEE_ROLE_FAILED to R.string.create_employee_role_failed,
        TypeMessage.UPDATE_EMPLOYEE_ROLE_FAILED to R.string.update_employee_role_failed,
        TypeMessage.DELETE_EMPLOYEE_ROLE_FAILED to R.string.delete_employee_role_failed,
        TypeMessage.CANT_DELETE_EMPLOYEE to R.string.cant_delete_employee,
        TypeMessage.CANT_CREATE_EMPLOYEE to R.string.cant_create_employee,
        TypeMessage.CANT_UPDATE_EMPLOYEE to R.string.cant_update_employee,
        TypeMessage.CANT_CREATE_REQUESTS to R.string.cant_create_requests,
        TypeMessage.CANT_UPDATE_EMPLOYEE_ROLE to R.string.cant_update_employee_role,
        TypeMessage.CREATE_NEW_EMPLOYEE_ROLE_FAILED to R.string.create_new_employee_role_failed,
        TypeMessage.DELETE_EMPLOYEE_FAILED to R.string.delete_employee_failed,
        TypeMessage.UPDATE_EMPLOYEE_FAILED to R.string.update_employee_failed,
        TypeMessage.CREATE_CLASS_FAILED to R.string.create_class_failed,
        TypeMessage.CANT_CREATE_CLASS_FAILED to R.string.cant_create_class_failed,
        TypeMessage.REPEAT_UNTIL_DATE_INVALID to R.string.repeat_until_date_invalid,
        TypeMessage.CONCLIC_EMPLOYEE_SCHEDULE to R.string.conclic_employee_schedule,
        TypeMessage.UNIQUE_DATE_INVALID to R.string.unique_date_invalid,
        TypeMessage.EMPLOYEE_NO_PERMISSION_INSTRUCTOR to R.string.employee_no_permission_instructor,
        TypeMessage.NO_STUDENTS_CAPACITY to R.string.no_students_capacity,
        TypeMessage.CONCLIC_ROOM_SCHEDULE to R.string.conclic_room_schedule,
        TypeMessage.AGE_RANGE_INVALID to R.string.age_range_invalid,
        TypeMessage.NAME_ALREADY_EXIST_IN_CLASS to R.string.name_already_exist_in_class,
        TypeMessage.UPDATE_CLASS_FAILED to R.string.update_class_failed,
        TypeMessage.CANT_UPDATE_CLASS to R.string.cant_update_class,
        TypeMessage.DELETE_CLASS_FAILED to R.string.delete_class_failed,
        TypeMessage.CANT_REMOVE_CLASS to R.string.cant_remove_class,
    )

    var errorMessage: Int? by mutableStateOf(null)
    var errorAction: (() -> Unit)? by mutableStateOf(null)
    var errorActionName: Int? by mutableStateOf(null)

    fun showError(type: TypeMessage) {
        val resId = errorMessages[type] ?: return
        showError(resId)
    }

    fun showError(idMessage: Int, onRetryAction: (() -> Unit)? = null, errorName: Int? = null) {
        NotifyHandler.clear()
        errorMessage = idMessage
        errorAction = onRetryAction
        errorActionName = errorName
    }

    fun clear() {
        errorMessage = null
        errorAction = null
        errorActionName = null
    }
}