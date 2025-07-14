package com.msfauthentication.presentation.dialog

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msfauthentication.R

private var loadingDialog: AlertDialog? = null

fun ComponentActivity.showLoadingDialog(
    isLoading: Boolean,
) {
    if (isLoading) {
        if (loadingDialog == null) {
            val customView = LayoutInflater.from(this).inflate(R.layout.loading_dialog_view, null)

            loadingDialog = MaterialAlertDialogBuilder(this)
                .setView(customView)
                .setCancelable(false)
                .create()
        }
        loadingDialog?.show()
    } else {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}