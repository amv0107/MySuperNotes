package com.amv.simple.app.mysupernotes.presentation.core

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragment
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.favoriteList.FavoriteFragment
import com.amv.simple.app.mysupernotes.presentation.favoriteList.FavoriteFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListAdapter
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragment
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListViewModel
import com.amv.simple.app.mysupernotes.presentation.trashList.TrashFragment
import com.amv.simple.app.mysupernotes.presentation.trashList.TrashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseListFragment : BaseFragment() {

    private var _binding: FragmentMainListBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<MainListViewModel>()

    lateinit var noteItemAdapter: MainListAdapter

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
                val action: NavDirections = when (this@BaseListFragment) {
                    is ArchiveListFragment -> ArchiveListFragmentDirections
                        .actionArchiveListFragmentToEditorFragment().setNoteId(noteItem.id)

                    is FavoriteFragment -> FavoriteFragmentDirections
                        .actionFavoriteListFragmentToEditorFragment().setNoteId(noteItem.id)

                    is TrashFragment -> TrashFragmentDirections
                        .actionTrashListFragmentToEditorFragment().setNoteId(noteItem.id)

                    else -> MainListFragmentDirections
                        .actionMainListFragmentToEditorFragment().setNoteId(noteItem.id)
                }

                Navigation.findNavController(view).navigate(action)
            }

            override fun onItemAction(noteItem: NoteItem) {
                BottomSheet.show(noteItem.title, parentFragmentManager) {
                    action(
                        titleResId = R.string.action_pin,
                        iconResId = R.drawable.ic_pin,
                        condition = (this@BaseListFragment is MainListFragment
                                || this@BaseListFragment is FavoriteFragment)
                                && !noteItem.isPinned
                    ) {
                        viewModel.changePin(noteItem)
                        Toast.makeText(requireContext(), R.string.edit_toast_pinned, Toast.LENGTH_SHORT).show()
                    }
                    action(
                        titleResId = R.string.action_unpin,
                        iconResId = R.drawable.ic_un_pin,
                        condition = (this@BaseListFragment is MainListFragment
                                || this@BaseListFragment is FavoriteFragment)
                                && noteItem.isPinned
                    ) {
                        viewModel.changePin(noteItem)
                        Toast.makeText(requireContext(), R.string.edit_toast_unpinned, Toast.LENGTH_SHORT).show()
                    }
                    action(
                        titleResId = R.string.action_send,
                        iconResId = R.drawable.ic_share,
                        condition = this@BaseListFragment is MainListFragment
                                || this@BaseListFragment is FavoriteFragment
                    ) {
                        Toast.makeText(requireContext(), "Share", Toast.LENGTH_SHORT).show()
                    }
                    action(
                        titleResId = R.string.action_unarchive,
                        iconResId = R.drawable.ic_un_favorite,
                        condition = this@BaseListFragment is ArchiveListFragment
                    ) {
                        viewModel.changeArchive(noteItem)
                        Toast.makeText(requireContext(), "UpArchive", Toast.LENGTH_SHORT).show()
                    }
                    action(
                        titleResId = R.string.action_regain_access,
                        iconResId = R.drawable.ic_restore,
                        condition = this@BaseListFragment is TrashFragment
                    ) {
                        viewModel.restoreDelete(noteItem)
                        Toast.makeText(requireContext(), "Restore", Toast.LENGTH_SHORT).show()
                    }
                    action(
                        R.string.action_delete,
                        R.drawable.ic_delete,
                        condition = this@BaseListFragment !is TrashFragment
                    ) {
                        viewModel.moveToTrash(noteItem)
                        Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
                    }
                    action(
                        R.string.action_delete,
                        R.drawable.ic_delete,
                        condition = this@BaseListFragment is TrashFragment
                    ) {
                        viewModel.deleteForeverNoteItem(noteItem)
                        Toast.makeText(requireContext(), "Delete Forever", Toast.LENGTH_SHORT).show()
                    }
                }
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