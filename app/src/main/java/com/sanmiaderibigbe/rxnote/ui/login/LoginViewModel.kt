package com.sanmiaderibigbe.rxnote.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository
import com.sanmiaderibigbe.rxnote.ui.repo.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    /***
     * login resource contains the authentication state of the current user.
     */
    private val loginResource = MutableLiveData<Resource<AuthenticationState>>()

    private val disposable = CompositeDisposable()

    init {
        when {
            firebaseRepository.getCurrentUser() != null -> {
                loginResource.value = Resource.loaded(AuthenticationState.AUTHENTICATED)
            }
            else -> {
                loginResource.value = Resource.loaded(AuthenticationState.UNAUTHENTICATED)
            }
        }
    }

    /**
     * Sign in user to app
     *
     *
     * @author Oluwasanmi Aderibigbe
     */
    fun signInUser(email: String, password: String) {
        loginResource.value = Resource.loading(AuthenticationState.UNAUTHENTICATED)
        disposable.add(
            firebaseRepository.signInUser(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: AuthResult? -> updateAuthenticationState(result) },
                    { throwable: Throwable -> updateLoginError(throwable) })
        )

    }

    /***
     *
     * @author Oluwasanmi Aderibigbe
     */
    private fun updateLoginError(throwable: Throwable) {
        loginResource.value = Resource.error(throwable.message, AuthenticationState.UNAUTHENTICATED)
    }

    /***
     * updates the authentication state of the user.
     * @author Oluwasanmi Aderibigbe
     */
    private fun updateAuthenticationState(result: AuthResult?) {

        when {
            result?.user != null -> {
                loginResource.value = Resource.success(AuthenticationState.AUTHENTICATED)
            }
            else -> {
                loginResource.value = Resource.error("Failed to Login", AuthenticationState.UNAUTHENTICATED)
            }
        }

    }

    fun getUserLoginResource(): LiveData<Resource<AuthenticationState>> {
        return loginResource
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }

    fun signOut() {
       firebaseRepository.signOut()
        loginResource.value = Resource.loaded(AuthenticationState.UNAUTHENTICATED)
    }


    enum class AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED,
    }
}