package com.sanmiaderibigbe.rxnote.ui.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanmiaderibigbe.rxnote.ui.login.LoginViewModelFactory
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository

object Injection {

    /***
     * Injects firebaseRepository when needed.
     * @param [application]    Application
     *
     * @return [FirebaseRepository]
     * @author Oluwasanmi Aderibigbe
     */
    fun provideFirebaseRepository(application: Application): FirebaseRepository {
        val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val  firebaseDatabase = FirebaseDatabase.getInstance()
        return FirebaseRepository(application, firebaseAuth, firebaseDatabase)
    }

    /***
     * Injects loginViewModel when needed.
     * @param [application]    Application
     *
     * @return [LoginViewModelFactory]
     * @author Oluwasanmi Aderibigbe
     */
    fun provideLoginModelFactory(application: Application) : LoginViewModelFactory {
        val firebaseRepository = provideFirebaseRepository(application)
        return LoginViewModelFactory(firebaseRepository)
    }
}