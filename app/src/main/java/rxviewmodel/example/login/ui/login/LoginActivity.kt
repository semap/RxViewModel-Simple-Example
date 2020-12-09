package rxviewmodel.example.login.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import rxviewmodel.example.login.R
import rxviewmodel.example.login.viewmodel.LoginAction.*
import rxviewmodel.example.login.viewmodel.LoginViewModel
import semap.rx.viewmodel.ActionExecutionMode
import semap.rx.viewmodel.asLiveData
import semap.rx.viewmodel.observe

class LoginActivity: AppCompatActivity() {

    private lateinit var signInButton: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginProgress: View
    private lateinit var loginForm: View

    private val viewModel: LoginViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
        viewModelProvider.get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        signInButton = findViewById(R.id.signInButton)
        loginProgress = findViewById<View>(R.id.loginProgress)
        loginForm = findViewById<View>(R.id.loginForm)

        // bind the LiveData in the ViewModel to the views.
        bindViewModelToView()
        // bind users inputs to the data in the ViewModel
        bindViewToViewModel()
    }

    private fun bindViewToViewModel() {

        username.doAfterTextChanged {
            if (it != null) {
                viewModel.execute(SetUsername(it.toString()))
            }
        }

        password.doAfterTextChanged {
            if (it != null) {
                viewModel.execute(SetPassword(it.toString()))
            }
        }

        signInButton.setOnClickListener {
            // By default, all the actions are executed in parallel.
            // And we want the Login action to be executed after all the previous actions ( SetUsername and SetPassword) are done (state is updated).
            // So we make Login action to be executed in ParallelDefer mode.
            viewModel.execute(Login, ActionExecutionMode.ParallelDefer)
            closeKeyboard()
        }
    }

    private fun bindViewModelToView() {
        viewModel.isLoading
                .observe(this, ::showProgress)

        viewModel.isFormValid
                .observe(this, signInButton::setEnabled)

        viewModel.errorObservable
                .asLiveData(viewModel)
                .observe(this, ::showError)

        viewModel.loginAction
                .observe(this) { showLoginStatus() }
    }

    private fun showProgress(show: Boolean) {
        loginProgress.visibility = if (show) View.VISIBLE else View.GONE
        loginForm.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showLoginStatus() {
        Toast.makeText(this, R.string.sign_in_successfully, Toast.LENGTH_LONG).show()
    }

    private fun showError(throwable: Throwable) {
        AlertDialog.Builder(this)
                .setTitle(R.string.error_login_failed_title)
                .setMessage(throwable.message)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}