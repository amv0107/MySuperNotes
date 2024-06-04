package com.amv.simple.app.mysupernotes.presentation.categoryList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.data.category.CategoryItemMapper
import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import com.amv.simple.app.mysupernotes.databinding.FragmentCategoryListBinding
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import com.amv.simple.app.mysupernotes.presentation.core.dialogs.infoDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryListViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryListBinding.bind(view)

        viewModel.getCategoryAndNote()

        viewModel.categoryAndNote.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ErrorResult -> {}
                is PendingResult -> {}
                is SuccessResult -> {
                    categoryAdapter.submitList(result.takeSuccess())
                }
            }
        }

        categoryAdapter = CategoryListAdapter(object : CategoryListAdapter.CategoryListListener {
            override fun onChooseCategory(view: View, categoryItem: CategoryAndNote) {
                val action: NavDirections = CategoryListFragmentDirections
                    .actionCategoryListFragmentToListOfNotesByCategoryOrTag(
                        categoryItem.category.id,
                        categoryItem.category.name
                    )
                Navigation.findNavController(view).navigate(
                    action, navOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in)
                        .setExitAnim(R.anim.fade_out)
                        .setPopEnterAnim(R.anim.fade_in)
                        .setPopExitAnim(R.anim.slide_out)
                        .build()
                )
            }

            override fun onEditCategory(categoryItem: CategoryAndNote) {
                EditorCategoryDialog.show(
                    parentFragmentManager,
                    categoryItem.category.id,
                    categoryItem.category.name
                )
            }

            override fun onDeleteCategory(categoryItem: CategoryAndNote) {
                // TODO: Мне кажется здесь допущена архитектурная ошибка
                val item = CategoryItemMapper().mapDbModelToEntity(categoryItem.category)
                viewModel.deleteCategoryItem(item)
            }
        })

        binding.ivInfo.setOnClickListener {
            infoDialog(R.string.info_category_list_fragment)
        }

        binding.rvCategoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryList.adapter = categoryAdapter

        binding.fabCreateCategory.setOnClickListener {
            EditorCategoryDialog.show(parentFragmentManager)
        }

        EditorCategoryDialog.setupListener(
            parentFragmentManager,
            this
        ) { idOfCategory, nameOfCategory ->
            if (idOfCategory == 0)
                viewModel.addCategoryItem(categoryName = nameOfCategory)
            else {
                viewModel.addCategoryItem(idOfCategory, nameOfCategory)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}