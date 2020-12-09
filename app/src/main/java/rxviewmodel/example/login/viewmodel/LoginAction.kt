package rxviewmodel.example.login.viewmodel

sealed class LoginAction {
    data class SetUsername(val username: String): LoginAction()
    data class SetPassword(val password: String): LoginAction()
    object Login: LoginAction()
}