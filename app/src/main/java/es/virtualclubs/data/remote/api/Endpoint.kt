package es.virtualclubs.data.remote.api

internal object Endpoint {

    private const val AUTH = "/v1/auth/"
    const val login                = "${AUTH}login"
    const val register             = "${AUTH}register"
    const val logout               = "${AUTH}logout"
    const val google               = "${AUTH}google"
    const val requestPasswordReset = "${AUTH}requestPasswordReset"
    const val resetPassword        = "${AUTH}resetPassword"
    const val requestVerify        = "${AUTH}requestVerify"
    const val refresh              = "${AUTH}refresh"

    private const val USER = "/v1/user/"
    const val getUserInfo  = "${USER}getUserInfo"
}
