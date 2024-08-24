package app.netlify.dev4rju9.runningtracker.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.netlify.dev4rju9.runningtracker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CancelTrackingDialog : DialogFragment() {

    private var positiveListener: (() -> Unit)? = null

    fun setPositiveListener (listener: () -> Unit) {
        this.positiveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                positiveListener?.let { it() }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

}