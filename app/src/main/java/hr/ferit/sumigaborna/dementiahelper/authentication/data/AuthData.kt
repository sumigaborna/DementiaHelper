package hr.ferit.sumigaborna.dementiahelper.authentication.data

import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING

data class AuthData(
    var registerSuccess : Boolean = false,
    var registerSuccessMessage: String = EMPTY_STRING,
    var registerErrorMessage: String = EMPTY_STRING,
    var loginSuccess : Boolean = false,
    var loginSuccessMessage: String = EMPTY_STRING,
    var loginErrorMessage: String = EMPTY_STRING
    )