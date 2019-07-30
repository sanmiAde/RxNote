package com.sanmiaderibigbe.rxnote.ui.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanmiaderibigbe.rxnote.ui.login.LoginViewModelFactory
import com.sanmiaderibigbe.rxnote.ui.register.RegisterViewModel
import com.sanmiaderibigbe.rxnote.ui.register.RegisterViewModelFactory
import com.sanmiaderibigbe.rxnote.ui.repo.FirebaseRepository

object Injection {

    private  var  firebaseAuth : FirebaseAuth? = null
    private  var  firebaseDatabase : FirebaseDatabase? = null
    /***
     * Injects firebaseRepository when needed.
     * @param [application]    Application
     *
     * @return [FirebaseRepository]
     * @author Oluwasanmi Aderibigbe
     */
    fun provideFirebaseRepository(application: Application): FirebaseRepository {

        return FirebaseRepository(application, firebaseAuth!!, firebaseDatabase!!)
    }

    /***
     * Injects loginViewModel when needed.
     * @param [application]    Application
     *
     * @return [LoginViewModelFactory]
     * @author Oluwasanmi Aderibigbe
     */
    fun provideLoginModelFactory(application: Application) : LoginViewModelFactory {

        firebaseDatabase = provideFireBaseDatabase()
        firebaseAuth = provideFirebaseAuth()

        val firebaseRepository = provideFirebaseRepository(application)
        return LoginViewModelFactory(firebaseRepository)
    }

    fun provideLRegisterModelFactory(application: Application) : RegisterViewModelFactory {
        val firebaseRepository = provideFirebaseRepository(application)
        return RegisterViewModelFactory(firebaseRepository)
    }

    fun provideFireBaseDatabase(): FirebaseDatabase {
        if(firebaseDatabase == null) {
           return FirebaseDatabase.getInstance()

        }
        return firebaseDatabase!!
    }

    fun provideFirebaseAuth() : FirebaseAuth {
        if(firebaseAuth == null) {
            return FirebaseAuth.getInstance()
        }
        return firebaseAuth!!
    }
}