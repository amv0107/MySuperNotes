package com.amv.simple.app.mysupernotes.presentation.favoriteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import com.amv.simple.app.mysupernotes.presentation.core.renderSimpleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseListFragment() {

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNoteList(favoriteNotes = true)
        viewModel.noteList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    noteItemAdapter.submitList(it)
                }
            )
        }

        binding.rvMainList.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteItemAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}