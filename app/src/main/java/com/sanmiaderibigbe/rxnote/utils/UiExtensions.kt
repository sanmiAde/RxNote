package com.sanmiaderibigbe.rxnote.utils

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout


    fun TextInputLayout.getEditable() : Editable {
        return this.editText?.text!!
    }

    fun TextInputLayout.getInputString() : String {
        return this.editText?.text.toString()
    }
