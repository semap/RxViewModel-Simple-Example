package rxviewmodel.example.login.data

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.security.auth.login.LoginException

/**
 * The mock login service
 */
open class LoginService {

    // returns an observable of token
    open fun loginToServer(username: String, password: String): Observable<String> {
        if ("error" == username && "error" == password) { // exception happens in a unexpected place
            throw RuntimeException("Something unexpected came up")
        }
        return Observable.just("mock_token")
                .delay(3, TimeUnit.SECONDS) // delay 3 secs to make it like a real API call
                .doOnNext {
                    if ("admin" != username || "admin" != password) {
                        throw LoginException("Wrong username password")
                    }
                }
    }
}