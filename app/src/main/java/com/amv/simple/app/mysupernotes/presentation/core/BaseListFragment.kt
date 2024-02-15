package com.amv.simple.app.mysupernotes.presentation.core

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragment
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListAdapter
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragment
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseListFragment : BaseFragment() {

    val viewModel by viewModels<MainListViewModel>()

    lateinit var noteItemAdapter: MainListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionMenu()

        noteItemAdapter = MainListAdapter(object : MainListAdapter.MainListListener {
            override fun onChooseNote(noteItem: NoteItem) {
                val action: NavDirections = when (this@BaseListFragment) {
                    is ArchiveListFragment -> ArchiveListFragmentDirections
                        .actionArchiveListFragmentToEditorFragment().setNoteId(noteItem.id)

                    else -> MainListFragmentDirections
                        .actionMainListFragmentToEditorFragment().setNoteId(noteItem.id)

                }

                Navigation.findNavController(view).navigate(action)
            }

            override fun onItemAction(noteItem: NoteItem) {
                Toast.makeText(requireContext(), "Bottom", Toast.LENGTH_SHORT).show()
            }
        })
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

}