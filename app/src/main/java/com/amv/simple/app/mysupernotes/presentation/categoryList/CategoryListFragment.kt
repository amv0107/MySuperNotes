package com.amv.simple.app.mysupernotes.presentation.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentCategoryListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryListBinding.bind(view)

        binding.fabCreateCategory.setOnClickListener {
            EditorCategoryDialog.show(parentFragmentManager)
        }

        EditorCategoryDialog.setupListener(parentFragmentManager, this) { nameOfCategory ->
            viewModel.addCategoryItem(name = nameOfCategory, position = 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}