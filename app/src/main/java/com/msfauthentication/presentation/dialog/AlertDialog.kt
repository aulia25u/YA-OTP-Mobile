package com.msfauthentication.presentation.dialog

import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msfauthentication.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun ComponentActivity.showAlertDialog(
    message: String?,
    properties: AlertDialogProperties = AlertDialogDefaults.alertDialogAutoDismiss(),
    onDismiss: () -> Unit = {},
) {
    val customView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null)
    customView.findViewById<TextView>(R.id.tvMessage).text = message

    val dialog = MaterialAlertDialogBuilder(this)
        .setView(customView)
        .setCancelable(properties.isCancelable)
        .create()

    dialog.show()

    if (properties.isAutoDismiss) {
        lifecycleScope.launch {
            delay(properties.dismissDelay)
            dialog.dismiss()
            onDismiss()
        }
    }
}

data class AlertDialogProperties(
    val isCancelable: Boolean,
    val isAutoDismiss: Boolean,
    val dismissDelay: Duration,
)

object AlertDialogDefaults {

    fun alertDialogAutoDismiss(
        isCancelable: Boolean = false,
        isAutoDismiss: Boolean = true,
        dismissDelay: Duration = 2.seconds,
    ) = AlertDialogProperties(
        isCancelable = isCancelable,
        isAutoDismiss = isAutoDismiss,
        dismissDelay = dismissDelay,
    )
}