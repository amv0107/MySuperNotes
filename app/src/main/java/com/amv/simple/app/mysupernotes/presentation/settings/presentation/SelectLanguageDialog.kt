package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreLanguage

class SelectLanguageDialog : DialogFragment() {

    private val currentLanguage: Int
        get() = requireArguments().getInt(ARG_VALUE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listLanguage = DataStoreLanguage.values().map {
            it.lang
        }.toTypedArray()

        return AlertDialog.Builder(requireContext(), R.style.ConfirmDialogFragment)
            .setTitle(R.string.theme_confirm_dialog_language_title)
            .setSingleChoiceItems(listLanguage, currentLanguage, null)
            .setNegativeButton(R.string.dialog_cancel) { _, _ ->
                dismiss()
            }
            .setPositiveButton(R.string.dialog_select) { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_VALUE_RESPONSE to index))
                dismiss()
            }
            .create()
    }

    companion object {
        @JvmStatic
        private val TAG = SelectLanguageDialog::class.java.simpleName

        @JvmStatic
        private val KEY_VALUE_RESPONSE = "KEY_VALUE_RESPONSE"

        @JvmStatic
        private val ARG_VALUE = "ARG_VALUE"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, currentLanguage: Int) {
            val dialogFragment = SelectLanguageDialog()
            dialogFragment.arguments = bundleOf(ARG_VALUE to currentLanguage)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_VALUE_RESPONSE))
            })
        }
    }
}