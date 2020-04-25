package com.htec.task

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.htec.task.utils.ConnectivityUtil
import com.htec.task.utils.DialogUtil
import com.htec.task.utils.SharedUtil

class LoadingActivity : AppCompatActivity() {

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    override fun onStart() {
        super.onStart()

        alertDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        if ((SharedUtil.hasLocalCache(this) && !SharedUtil.isLocalCacheExpired(this))
            || ConnectivityUtil.hasActiveInternetConnection(this)
        ) {
            // fake delay as part of client-server communication
            Handler().postDelayed({
                startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, 2000)
        } else {
            showNetworkDialog()
        }
    }

    private fun showNetworkDialog() {

        var positiveClickListener: (dialog: DialogInterface, which: Int) -> Unit =
            { dialog, _ ->
                // fake delay as part of client-server communication
                Handler(Looper.getMainLooper()).postDelayed({
                    dialog.dismiss()
                    checkNetworkConnection()
                }, 1000)
            }

        var negativeClickListener: (dialog: DialogInterface, which: Int) -> Unit =
            { dialog, _ ->
                dialog.dismiss()
                finish()
            }

        var neutralClickLitener: (dialog: DialogInterface, which: Int) -> Unit = { dialog, _ ->
            dialog.dismiss()
            val intent: Intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }

        alertDialog = DialogUtil.showCustomDialogWithLambdaExpression(
            this,
            title = applicationContext.resources.getString(R.string.dialog_network_connection_title),
            message = applicationContext.resources.getString(R.string.dialog_network_connection_message),
            cancelable = false,
            positiveButtonText = applicationContext.getString(R.string.dialog_network_connection_positive_button),
            negativeButtonText = applicationContext.getString(R.string.dialog_network_connection_negative_button),
            neutralButtonText = applicationContext.getString(R.string.dialog_network_connection_neutral_button),
            onPositiveClickListener = positiveClickListener,
            onNegativeClickListener = negativeClickListener,
            onNeutralClickListener = neutralClickLitener
        )
    }
}
