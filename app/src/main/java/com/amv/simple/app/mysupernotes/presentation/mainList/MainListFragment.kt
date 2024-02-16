package com.amv.simple.app.mysupernotes.presentation.mainList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.BaseFragment
import com.amv.simple.app.mysupernotes.presentation.core.renderSimpleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : BaseFragment() {

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
        optionMenu()

        binding.fabCrateNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainListFragment_to_editorFragment)
        }
        viewModel.getNoteList(TypeList.MainList)

        viewModel.noteList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    noteItemAdapter.submitList(it)
                }
            )
        }

        noteItemAdapter = MainListAdapter(object : MainListAdapter.MainListListener {
            override fun onChooseNote(noteItem: NoteItem) {
                val action: NavDirections = MainListFragmentDirections
                    .actionMainListFragmentToEditorFragment().setNoteId(noteItem.id)

                Navigation.findNavController(view).navigate(action)
            }

            override fun onItemAction(noteItem: NoteItem) {
                Toast.makeText(requireContext(), "Bottom", Toast.LENGTH_SHORT).show()
            }
        })

        binding.rvMainList.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteItemAdapter
        }
    }

    private fun optionMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.list_menu_search -> {}
                    R.id.list_menu_type_layout_manager -> {}
                    R.id.list_menu_sort -> {}
                }
                return false
            }
        }, viewLifecycleOwner)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}