package com.amv.simple.app.mysupernotes.presentation.mainList

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import com.amv.simple.app.mysupernotes.presentation.core.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : BaseListFragment() {

    private lateinit var filterByCategoryAdapter: FilterByCategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrateNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_editorFragment)
        }

        viewModel.getCategoryList()

        filterByCategoryAdapter = FilterByCategoryAdapter(object : FilterByCategoryAdapter.FilterByCategoryListener{
            override fun onClick(categoryItemId: Int?) {
                viewModel.setFilterByCategoryId(categoryItemId)
            }
        })

        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            filterByCategoryAdapter.submitList(list)
        }

        binding.rvFilterByCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilterByCategory.adapter = filterByCategoryAdapter
        (binding.rvFilterByCategory.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.filterByCategoryId.observe(viewLifecycleOwner) {idCategory ->
            viewModel.getNoteList(TypeList.MAIN_LIST, idCategory)
        }
    }
}