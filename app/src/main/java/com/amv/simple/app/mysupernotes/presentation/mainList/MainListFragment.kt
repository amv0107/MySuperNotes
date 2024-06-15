package com.amv.simple.app.mysupernotes.presentation.mainList

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : BaseListFragment() {

    private lateinit var filterByCategoryAdapter: FilterByCategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.filterByCategoryId.observe(viewLifecycleOwner) { id ->
            viewModel.getNoteList(TypeList.MAIN_LIST)
            val selectedItem = id?.minus(1) ?: 0 // -1 Because we deleted "Without category"
            initFilterByCategory(selectedItem)

            binding.fabCrateNote.setOnClickListener {
                val action = MainListFragmentDirections.actionMainListFragmentToEditorFragment()
                    .setSelectedCategoryId(selectedItem + 1)
                findNavController().navigate(action)
            }
        }
    }

    private fun initFilterByCategory(selectedItem: Int) {
        viewModel.getCategoryList()

        filterByCategoryAdapter =
            FilterByCategoryAdapter(
                selectedItem,
                object : FilterByCategoryAdapter.FilterByCategoryListener {
                    override fun onClickItem(categoryItemId: Int) {
                        viewModel.setFilterByCategoryId(categoryItemId)
                    }

                    override fun onClickAll() {
                        viewModel.setFilterByCategoryId(0)
                    }

                    override fun onClickAdd() {
                        findNavController().navigate(
                            R.id.categoryListFragment, null, navOptions = NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in)
                                .setExitAnim(R.anim.fade_out)
                                .setPopEnterAnim(R.anim.fade_in)
                                .setPopExitAnim(R.anim.slide_out)
                                .build()
                        )
                    }
                })

        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            filterByCategoryAdapter.submitList(list)
        }


        binding.rvFilterByCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilterByCategory.adapter = filterByCategoryAdapter
        (binding.rvFilterByCategory.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
    }
}