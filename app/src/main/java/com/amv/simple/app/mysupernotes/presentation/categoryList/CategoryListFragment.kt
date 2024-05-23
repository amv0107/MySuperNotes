package com.amv.simple.app.mysupernotes.presentation.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentCategoryListBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryListViewModel by viewModels()
    lateinit var categoryAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryListBinding.bind(view)

        viewModel.getCategoryList()

        viewModel.categoryList.observe(viewLifecycleOwner) { result ->
            when(result) {
                is ErrorResult -> {}
                is PendingResult -> {}
                is SuccessResult -> categoryAdapter.submitList(result.takeSuccess())
            }
        }

        categoryAdapter = CategoryListAdapter(object : CategoryListAdapter.CategoryListListener {
            override fun onChooseCategory(categoryItem: CategoryItem) {
                Toast.makeText(requireContext(), "onClickListener", Toast.LENGTH_SHORT).show()
            }

            override fun onEditCategory(categoryItem: CategoryItem) {
                EditorCategoryDialog.show(parentFragmentManager, categoryItem.id, categoryItem.name)
            }

            override fun onDeleteCategory(categoryItem: CategoryItem) {
                Toast.makeText(requireContext(), "delete item", Toast.LENGTH_SHORT).show()
            }
        })

        binding.rvCategoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryList.adapter = categoryAdapter

        binding.fabCreateCategory.setOnClickListener {
            EditorCategoryDialog.show(parentFragmentManager)
        }

        EditorCategoryDialog.setupListener(parentFragmentManager, this) { idOfCategory, nameOfCategory ->
            if (idOfCategory == null)
                viewModel.addCategoryItem(nameCategory = nameOfCategory, positionCategory = 1)
            else {
                viewModel.addCategoryItem(idOfCategory, nameOfCategory,1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}