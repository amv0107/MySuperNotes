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

/**
 * По урокам Романа Андрющенко: https://www.youtube.com/watch?v=wDH5XCai6zI&list=PLRmiL0mct8WnodKkGLpBN0mfXIbAAX-Ux&index=15&t=959s&pp=iAQB
 */
class EditorCategoryDialog : DialogFragment(R.layout.dialog_editor_category) {

    private var _binding: DialogEditorCategoryBinding? = null
    val binding get() = _binding!!

    private val idCategory: Int
        get() = requireArguments().getInt(ARG_ID)
    private val nameCategory: String
        get() = requireArguments().getString(ARG_NAME).toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogEditorCategoryBinding.bind(view)

        if (nameCategory.isNotEmpty()) {
            binding.labelTitleDialog.text = "Edit category"
            binding.etNameCategory.setText(nameCategory)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnApply.setOnClickListener {
            val nameOfCategory = binding.etNameCategory.text.toString()
            if (idCategory == 0)
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(KEY_CATEGORY_NAME_RESPONSE to nameOfCategory)
                )
            else
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(
                        KEY_CATEGORY_ID_RESPONSE to idCategory,
                        KEY_CATEGORY_NAME_RESPONSE to nameOfCategory)
                )

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
        private val KEY_CATEGORY_NAME_RESPONSE = "KEY_CATEGORY_NAME_RESPONSE"
        private val KEY_CATEGORY_ID_RESPONSE = "KEY_CATEGORY_ID_RESPONSE"

        @JvmStatic
        val ARG_NAME = "ARG_NAME"
        @JvmStatic
        val ARG_ID = "ARG_ID"

        @JvmStatic // Не уверен что здесь это нужно
        private val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, id: Int, name: String) {
            val dialogFragment = EditorCategoryDialog()
            dialogFragment.arguments = bundleOf(ARG_ID to id, ARG_NAME to name)
            dialogFragment.show(manager, TAG)
        }

        fun show(manager: FragmentManager) {
            val dialogFragment = EditorCategoryDialog()
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Int?, String) -> Unit
        ) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(
                    result.getInt(KEY_CATEGORY_ID_RESPONSE),
                    result.getString(KEY_CATEGORY_NAME_RESPONSE) as String
                )
            }
        }
    }
}