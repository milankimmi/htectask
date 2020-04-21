package com.htec.task.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.htec.task.R

object DialogUtil {

    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        cancelable: Boolean = true,
        positiveButtonText: String = context.getString(R.string.dialog_positive_button),
        negativeButtonText: String = context.getString(R.string.dialog_negative_button),
        neutralButtonText: String = context.getString(R.string.dialog_neutral_button),
        onPositiveClickListener: DialogInterface.OnClickListener? = null,
        onNegativeClickListener: DialogInterface.OnClickListener? = null,
        onNeutralClickListener: DialogInterface.OnClickListener? = null
    ) : AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        onPositiveClickListener?.let { builder.setPositiveButton(positiveButtonText, it) }
        onNegativeClickListener?.let { builder.setNegativeButton(negativeButtonText, it) }
        onNeutralClickListener?.let { builder.setNeutralButton(neutralButtonText, it) }
        builder.setCancelable(cancelable)
        return builder.show()
    }

    fun showCustomDialogWithLambdaExpression(
        context: Context,
        title: String,
        message: String,
        cancelable: Boolean = true,
        positiveButtonText: String = context.getString(R.string.dialog_positive_button),
        negativeButtonText: String = context.getString(R.string.dialog_negative_button),
        neutralButtonText: String = context.getString(R.string.dialog_neutral_button),
        onPositiveClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        onNegativeClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        onNeutralClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null
    ) : AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        onPositiveClickListener?.let { builder.setPositiveButton(positiveButtonText, it) }
        onNegativeClickListener?.let { builder.setNegativeButton(negativeButtonText, it) }
        onNeutralClickListener?.let { builder.setNeutralButton(neutralButtonText, it) }
        builder.setCancelable(cancelable)
        return builder.show()
    }
}