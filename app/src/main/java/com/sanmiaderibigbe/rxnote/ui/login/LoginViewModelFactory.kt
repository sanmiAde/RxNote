package com.sanmiaderibigbe.rxnote.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository

/***
 * Login view model factory class.
 * A custom view model factory class is created to inject firebase repository into the login view model class.
 * @author Oluwasanmi Aderibigbe
 */
class LoginViewModelFactory(private val firebaseRepository: FirebaseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(firebaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}