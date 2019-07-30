package com.sanmiaderibigbe.rxnote.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast

object UiUtils {

    fun createProgressDialog(activity : Activity): ProgressDialog {
       return ProgressDialog(activity)
    }

    fun initLoadingDialog(progressDialog: ProgressDialog, message : String) {

        progressDialog.setTitle(message)
        progressDialog.show()

    }

   fun stopLoadingDialog(progressDialog: ProgressDialog) {
        progressDialog.cancel()

    }

    fun createToastMessage(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}