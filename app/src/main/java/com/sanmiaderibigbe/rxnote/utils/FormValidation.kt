package com.sanmiaderibigbe.rxnote.utils

import android.text.Editable

object FormValidation {
    fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }

    fun isEmailValid(text: Editable?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text?.trim()).matches()
    }

    fun isTextNotBlank(text: Editable?): Boolean {
        return !text?.trim()?.isBlank()!!
    }
}