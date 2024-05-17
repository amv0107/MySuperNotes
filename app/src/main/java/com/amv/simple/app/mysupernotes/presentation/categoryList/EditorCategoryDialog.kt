package com.amv.simple.app.mysupernotes.presentation.categoryList

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.DialogEditorCategoryBinding

class EditorCategoryDialog : DialogFragment(R.layout.dialog_editor_category) {

    private var _binding: DialogEditorCategoryBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogEditorCategoryBinding.bind(view)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnApply.setOnClickListener {
            val nameOfCategory = binding.etNameCategory.text.toString()
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_CATEGORY_NAME_RESPONSE to nameOfCategory))
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic // Не уверен что здесь это нужно
        private val TAG = EditorCategoryDialog::class.java.simpleName

        @JvmStatic
        private val KEY_CATEGORY_NAME_RESPONSE = "KEY_NOTE_ORDER_RESPONSE"

        @JvmStatic // Не уверен что здесь это нужно
        private val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager) {
            val dialogFragment = EditorCategoryDialog()
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (String) -> Unit
        ) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getString(KEY_CATEGORY_NAME_RESPONSE) as String)
            }
        }
    }
}