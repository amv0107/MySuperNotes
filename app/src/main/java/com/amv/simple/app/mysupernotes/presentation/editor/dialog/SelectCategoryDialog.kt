package com.amv.simple.app.mysupernotes.presentation.editor.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentSelectCategoryBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import com.amv.simple.app.mysupernotes.presentation.categoryList.EditorCategoryDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectCategoryDialog @Inject constructor() :
    DialogFragment(R.layout.fragment_select_category) {

    private var _binding: FragmentSelectCategoryBinding? = null
    val binding get() = _binding!!

    private val idCategory: Int
        get() = requireArguments().getInt(ARG_ID)

    val viewModel: SelectCategoryViewModel by viewModels()
    private lateinit var recyclerViewAdapter: SelectCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.END + Gravity.TOP)
        val p = dialog?.window?.attributes
        p?.width = ViewGroup.LayoutParams.MATCH_PARENT
        p?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        p?.x = 50
        p?.y = 700

        dialog?.window?.attributes = p
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSelectCategoryBinding.bind(view)
        viewModel.getCategoryList()

        viewModel.listCategory.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ErrorResult -> {}
                is PendingResult -> {}
                is SuccessResult -> recyclerViewAdapter.submitList(result.takeSuccess())
            }
        }

        recyclerViewAdapter =
            SelectCategoryAdapter(idCategory, object : SelectCategoryAdapter.Listener {
                override fun onClick(categoryItem: CategoryItem) {
                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY, bundleOf(KEY_CATEGORY_ID to categoryItem.id)
                    )
                    dismiss()
                }
            })
        binding.rvCategoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryList.adapter = recyclerViewAdapter

        binding.btnAdd.setOnClickListener {
            dialog?.hide()
            EditorCategoryDialog.show(parentFragmentManager)
        }

        EditorCategoryDialog.setupListener(
            parentFragmentManager,
            viewLifecycleOwner
        ) { _, nameOfCategory ->
            viewModel.addCategoryItem(nameOfCategory)
            dialog?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        private val TAG = SelectCategoryDialog::class.simpleName

        @JvmStatic
        private val REQUEST_KEY = "$TAG:defaultRequestKey"
        private const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        private const val ARG_ID = "ARG_ID"

        fun show(manager: FragmentManager, idCategory: Int) {
            val dialogFragment = SelectCategoryDialog()
            dialogFragment.arguments = bundleOf(ARG_ID to idCategory)
            dialogFragment.show(manager, TAG)
        }

        fun setListener(
            manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit
        ) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(
                    result.getInt(KEY_CATEGORY_ID)
                )
            }
        }
    }
}