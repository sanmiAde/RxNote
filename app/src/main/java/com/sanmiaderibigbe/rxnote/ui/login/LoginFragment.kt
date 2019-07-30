package com.sanmiaderibigbe.rxnote.ui.login


import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.rxnote.R
import com.sanmiaderibigbe.rxnote.ui.di.Injection
import com.sanmiaderibigbe.rxnote.ui.repo.Status
import com.sanmiaderibigbe.rxnote.ui.todo.TodoFragmentDirections
import com.sanmiaderibigbe.rxnote.utils.FormValidation
import com.sanmiaderibigbe.rxnote.utils.UiUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {


    private lateinit var loginViewModel: LoginViewModel

    private lateinit var progressDialog: ProgressDialog

    private lateinit var navController: NavController


    override fun onStart() {
        super.onStart()
        getAuthenticationState()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        progressDialog = UiUtils.createProgressDialog(activity!!)

        navController = findNavController()

        btn_login.setOnClickListener {
            val emailEditText = text_email_input.editText
            val passwordEditText = text_password_input.editText

            val isFormValid = validateForm(emailEditText?.text, passwordEditText?.text)

            when {
                isFormValid -> {

                        loginViewModel.signInUser(
                            emailEditText?.text.toString(),
                            passwordEditText?.text.toString()
                        )

                    observeLoginResource()

                }
            }
        }


    }

    private fun initViewModel() {
        val viewModelFactory = Injection.provideLoginModelFactory(activity?.application!!)
        loginViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private fun observeLoginResource() {
        loginViewModel
            .getUserLoginResource()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> UiUtils.initLoadingDialog(progressDialog, "Login in...")
                    Status.ERROR -> {
                        UiUtils.stopLoadingDialog(progressDialog)
                        if (it.message != null) {

                        }
                            UiUtils.createToastMessage(it.message!!, context!!)
                    }
                    Status.LOADED -> {
                    }
                    Status.SUCCESS -> {
                        UiUtils.stopLoadingDialog(progressDialog)
                        if (it.data != null) {
                            if (it.data == LoginViewModel.AuthenticationState.AUTHENTICATED) {

                                navController.navigate(R.id.todoFragment)
                            }
                        }
                    }
                }
            })
    }

    /***
     * Validates login form
     * @param email The user's email
     * @param password THe user's password
     *
     * @return A boolean contains true of false indicating if the form is valid.
     */
    private fun validateForm(email: Editable?, password: Editable?): Boolean {
        val isEmailValid = FormValidation.isEmailValid(email)
        val isPasswordValid = FormValidation.isPasswordValid(password)

        when {
            isEmailValid -> text_email_input.error = null
            else -> text_email_input.error = getString(R.string.invalid_email)
        }

        when {
            isPasswordValid -> text_password_input.error = null
            else -> text_password_input.error = getString(R.string.invalid_password)
        }

        return isEmailValid && isPasswordValid
    }

    private fun getAuthenticationState() {
        loginViewModel.getUserLoginResource().observe(viewLifecycleOwner, Observer {
            when (it.data) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {navController.navigate(R.id.todoFragment)}
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {}
                null -> {}
            }
        })
    }
}
