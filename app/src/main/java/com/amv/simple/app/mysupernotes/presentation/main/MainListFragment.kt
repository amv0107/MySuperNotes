package com.amv.simple.app.mysupernotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem

class MainListFragment : Fragment() {

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainListViewModel>()
    private lateinit var noteItemAdapter: MainListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteItemAdapter = MainListAdapter(object : MainListAdapter.MainListListener {
            override fun onChooseNote(noteItem: NoteItem) {
                Toast.makeText(requireContext(), "Open", Toast.LENGTH_SHORT).show()
            }

            override fun onItemAction(noteItem: NoteItem) {
                Toast.makeText(requireContext(), "Bottom", Toast.LENGTH_SHORT).show()
            }
        })

        binding.fabCrateNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainListFragment_to_editorFragment)
        }

        viewModel.noteList.observe(viewLifecycleOwner) {
            noteItemAdapter.submitList(it)
        }

//        binding.rvMainList.layoutManager = LinearLayoutManager(
//            requireContext(),
//            LinearLayoutManager.VERTICAL,
//            false
//        )

        binding.rvMainList.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.rvMainList.adapter = noteItemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}