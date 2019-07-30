package com.sanmiaderibigbe.rxnote.ui.register


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
import com.sanmiaderibigbe.rxnote.ui.model.User
import com.sanmiaderibigbe.rxnote.ui.repo.Status
import com.sanmiaderibigbe.rxnote.utils.FormValidation
import com.sanmiaderibigbe.rxnote.utils.UiUtils
import com.sanmiaderibigbe.rxnote.utils.getEditable
import com.sanmiaderibigbe.rxnote.utils.getInputString
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterFragment : Fragment() {

    private lateinit var registerViewModel : RegisterViewModel

    private lateinit var progressDialog: ProgressDialog

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initViewModel()

        btn_register.setOnClickListener {
            val isFormValid = validateForm(txt_input_user_name.getEditable(), txt_input_email.getEditable(), txt_input_password.getEditable(), txt_input_verify_password.getEditable() )

            when {
                isFormValid -> {
                    val userName = txt_input_user_name.getInputString()
                    val email = txt_input_email.getInputString()
                    val password = txt_input_password.getInputString()
                    val user = User(userName, email)

                    registerViewModel.registerUser(user, password)

                    observeUserRegistration()
                }
            }
        }
    }

    private fun observeUserRegistration() {
        val progressDialog = UiUtils.createProgressDialog(activity!!)
        registerViewModel.getRegistrationResource().observe(this, Observer {
            when(it.status){
                Status.LOADING -> {UiUtils.initLoadingDialog(progressDialog, "Creating account...")}
                Status.ERROR -> {
                    UiUtils.stopLoadingDialog(progressDialog)
                    if(it.message != null) {
                        UiUtils.createToastMessage("Error occurred while creating account", context!!)
                    }
                }
                Status.LOADED -> {}
                Status.SUCCESS -> {
                    UiUtils.stopLoadingDialog(progressDialog)
                    if(it.data == true) {
                        navController.navigate(R.id.todoFragment)
                    }
                }
            }
        })
    }

    private fun initViewModel() {
        val viewModelFactory = Injection.provideLRegisterModelFactory(activity?.application!!)
        registerViewModel = ViewModelProviders.of(this, viewModelFactory)[RegisterViewModel::class.java]

    }

    private fun validateForm(userName : Editable, email : Editable, password : Editable, verifyPassword : Editable): Boolean {
        val isUserNameValid = FormValidation.isTextNotBlank(userName)

        val isEmailValid = FormValidation.isEmailValid(email)

        val isPasswordValid = FormValidation.isPasswordValid(password)

        val isVerifyPasswordValid = FormValidation.isPasswordValid(verifyPassword)

        val isPasswordVerified = FormValidation.isPasswordVerified(password, verifyPassword)

        when {
            isEmailValid -> txt_input_email.error = null
            else -> txt_input_email.error = getString(R.string.invalid_email)
        }

        when {
            isPasswordValid -> txt_input_password.error = null
            else -> txt_input_password.error = getString(R.string.invalid_password)
        }

        when {
            isUserNameValid -> {
                txt_input_user_name.error = null
            }
            else -> txt_input_user_name.error = getString(R.string.user_name_error)
        }

        when {
            isVerifyPasswordValid -> txt_input_verify_password.error = null
            else -> txt_input_verify_password.error = getString(R.string.invalid_password)
        }

        when {
            isPasswordVerified -> txt_input_verify_password.error = null
            else -> txt_input_verify_password.error = getString(R.string.password_not_the_same_error)
        }

        return isEmailValid && isPasswordValid && isUserNameValid && isVerifyPasswordValid && isPasswordVerified


    }
}
