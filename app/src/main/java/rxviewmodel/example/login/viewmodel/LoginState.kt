package rxviewmodel.example.login.viewmodel

data class LoginState(
        val username: String = "",
        val password: String = "",
        val token: String? = null)