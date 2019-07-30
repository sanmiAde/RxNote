package com.sanmiaderibigbe.rxnote.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository

class RegisterViewModelFactory(private val firebaseRepository: FirebaseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(firebaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}