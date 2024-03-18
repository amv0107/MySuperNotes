package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme

class SelectThemeDialog : DialogFragment() {

    private val currentTheme: Int
        get() = requireArguments().getInt(ARG_VALUE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listTheme = DataStoreTypeTheme.values().map {
            resources.getString(it.title)
        }.toTypedArray()

        return AlertDialog.Builder(requireContext(), R.style.ConfirmDialogFragment)
            .setTitle(R.string.theme_confirm_dialog_theme_title)
            .setSingleChoiceItems(listTheme, currentTheme, null)
            .setNegativeButton(R.string.theme_confirm_dialog_cancel) { _, _ ->
                dismiss()
            }
            .setPositiveButton(R.string.theme_confirm_dialog_select) { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_VALUE_RESPONSE to index))
                dismiss()
            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    companion object {
        @JvmStatic
        private val TAG = SelectThemeDialog::class.java.simpleName

        @JvmStatic
        private val KEY_VALUE_RESPONSE = "KEY_VALUE_RESPONSE"

        @JvmStatic
        private val ARG_VALUE = "ARG_VALUE"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, currentTheme: Int) {
            val dialogFragment = SelectThemeDialog()
            dialogFragment.arguments = bundleOf(ARG_VALUE to currentTheme)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_VALUE_RESPONSE))
            })
        }
    }
}
