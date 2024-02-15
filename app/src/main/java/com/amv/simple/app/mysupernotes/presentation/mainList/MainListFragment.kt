package com.amv.simple.app.mysupernotes.presentation.mainList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import com.amv.simple.app.mysupernotes.presentation.core.renderSimpleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : BaseListFragment() {

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

        binding.fabCrateNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainListFragment_to_editorFragment)
        }
        viewModel.getNoteList()
        viewModel.noteList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    noteItemAdapter.submitList(it)
                }
            )
        }

//        binding.rvMainList.layoutManager = LinearLayoutManager(
//            requireContext(),
//            LinearLayoutManager.VERTICAL,
//            false
//        )

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