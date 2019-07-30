package com.sanmiaderibigbe.rxnote.ui.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.rxnote.ui.login.LoginViewModel
import com.sanmiaderibigbe.rxnote.ui.model.User
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository
import com.sanmiaderibigbe.rxnote.ui.repo.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class RegisterViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val registerResource = MutableLiveData<Resource<Boolean>>()

    private val disposable = CompositeDisposable()

    fun registerUser(user : User, password : String) {
        registerResource.value = Resource.loading(false)

        disposable.add(
        firebaseRepository.registerUser(user, password)
            .flatMapCompletable {
                firebaseRepository.sendEmailVerification(it.user)

            }.andThen(firebaseRepository.uploadLoadUserData(user))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                registerResource.value = Resource.success(true)
            })
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }

    fun getRegistrationResource() : LiveData<Resource<Boolean>> = registerResource
}