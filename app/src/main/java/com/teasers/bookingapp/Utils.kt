package com.teasers.bookingapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

object Utils {

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        pText: String,
        nText: String,
        pListener: (DialogInterface, Int) -> Unit = { dialog: DialogInterface, _: Int -> dialog.dismiss() },
        nListener: (DialogInterface, Int) -> Unit = { dialog: DialogInterface, _: Int -> dialog.dismiss() }
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(pText, DialogInterface.OnClickListener(pListener))
        if (nText.isNotEmpty()) {
            builder.setNegativeButton(nText, DialogInterface.OnClickListener(nListener))
        }
        builder.show()
    }
}