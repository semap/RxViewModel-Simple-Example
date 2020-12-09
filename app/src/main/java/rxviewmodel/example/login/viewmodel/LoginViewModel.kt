package rxviewmodel.example.login.viewmodel

import io.reactivex.rxjava3.core.Observable
import rxviewmodel.example.login.data.LoginService
import semap.rx.viewmodel.RxViewModel
import semap.rx.viewmodel.Reducer
import rxviewmodel.example.login.viewmodel.LoginAction.*

class LoginViewModel(private val loginService: LoginService = LoginService()): RxViewModel<LoginAction, LoginState>() {

    // *** Begin of LiveData ***
    val isFormValid by lazy {
        stateObservable
            .map { it.username.length > 4 && it.password.length > 2 }
            .distinctUntilChanged()
            .asLiveData()
    }

    val isLoading by lazy {
        loadingObservable
                .asLiveData()
    }

    val loginActionComplete
        get() = actionOnCompleteObservable(Login::class.java)
                .asLiveData()

    val error
        get() = errorObservable
            .asLiveData()

    // *** End of LiveData ***

    override fun createReducerObservable(action: LoginAction): Observable<Reducer<LoginState>>? {
        return when (action) {
        is SetUsername -> Observable.just { oldState -> oldState.copy(username = action.username) }

        is SetPassword -> Observable.just { oldState -> oldState.copy(password = action.password) }

        is Login -> Observable.fromCallable { currentState }
                .flatMap { loginService.loginToServer(it.username, it.password) }
                .map { token -> { oldState: LoginState -> oldState.copy(token = token) } }
        }
    }

    override fun showSpinner(action: LoginAction): Boolean {
        return when (action) {
            is Login -> true
            else -> false
        }
    }

    override fun createInitialState(): LoginState =
            LoginState()

}