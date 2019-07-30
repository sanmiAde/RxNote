package com.sanmiaderibigbe.rxnote.ui.repo

import android.app.Application
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sanmiaderibigbe.rxnote.ui.model.User
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.schedulers.Schedulers

/***
 * Firebase repository class handles all transactions with firebase.
 * @param firebaseAuth Firbase Authentication object.
 * @param firebaseDatabase Firebase Database object.
 *
 * @author OLuwasanmi Aderibigbe
 */
class FirebaseRepository(
    private val application: Application,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {


    /***
     * Logs in user
     * @param [email] email of user
     * @param password password of user
     *@author Oluwasanmi Aderibigbe
     * @return [Maybe<AuthResult>] observable containing authresult. Auth result contains the currently signed user object.
     */
    fun signInUser(email: String, password: String): Maybe<AuthResult> {
        return initSignInUserTransaction(email, password).subscribeOn(Schedulers.io())

    }

    fun registerUser(user: User, password: String): Maybe<AuthResult> {
        return initRegisterUserTransaction(user, password).subscribeOn(Schedulers.io())
    }

    fun uploadLoadUserData(user: User): Completable {
        return Completable.create { emitter: CompletableEmitter ->
            firebaseDatabase.getReference(USERS_PATH)
                .child(getCurrentUser()?.uid!!)
                .setValue(user)
                .addOnCompleteListener {
                    if(!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
        }.subscribeOn(Schedulers.io())
    }

    fun sendEmailVerification(user: FirebaseUser): Completable {
        return Completable.create { emitter: CompletableEmitter ->
            user.sendEmailVerification().addOnCompleteListener {
                if (!emitter.isDisposed) {
                    emitter.onComplete()
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun initRegisterUserTransaction(user: User, password: String): Maybe<AuthResult> {
        return Maybe.create<AuthResult> { emitter: MaybeEmitter<AuthResult> ->
            firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {
                if (!emitter.isDisposed) {
                    emitter.onSuccess(it)
                }
            }.addOnFailureListener {
                if (!emitter.isDisposed) {
                    emitter.onError(it)
                }
            }
        }.subscribeOn(Schedulers.io())

    }

    private fun initSignInUserTransaction(email: String, password: String): Maybe<AuthResult> {
        return Maybe.create<AuthResult> { emitter: MaybeEmitter<AuthResult> ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                if (!emitter.isDisposed) {
                    emitter.onSuccess(it)
                }
            }.addOnFailureListener {
                if (!emitter.isDisposed) {
                    emitter.onError(it)
                }
            }
        }
    }

    /***
     * Gets current firebase user.
     * @return [FirebaseUser] current firebase user.
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        private const val USERS_PATH = "users"
    }
}