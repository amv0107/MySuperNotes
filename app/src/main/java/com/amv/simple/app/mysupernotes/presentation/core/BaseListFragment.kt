package com.amv.simple.app.mysupernotes.presentation.core

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.BuildConfig
import com.amv.simple.app.mysupernotes.databinding.FragmentMainListBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.util.ShareHelper
import com.amv.simple.app.mysupernotes.monetisation.loadBannerAds
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragment
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.favoriteList.FavoriteFragment
import com.amv.simple.app.mysupernotes.presentation.favoriteList.FavoriteFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListAdapter
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragment
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragmentDirections
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListViewModel
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import com.amv.simple.app.mysupernotes.presentation.trashList.TrashFragment
import com.amv.simple.app.mysupernotes.presentation.trashList.TrashFragmentDirections
import com.google.android.gms.ads.AdSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TAG = "BaseListFragment"

@AndroidEntryPoint
abstract class BaseListFragment : BaseFragment(R.layout.fragment_main_list) {

    private var _binding: FragmentMainListBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<MainListViewModel>()

    lateinit var noteItemAdapter: MainListAdapter
    private var mainMenu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainListBinding.bind(view)
        /**
         * optionMenu()
         * Отключил потому что при смене вида пока не перезагрузиш страницу не обновляется, баг появился после того
         * как добавил форматирование даты и времени. Связано все это как то с Flow
         * переместил функционал временно в настройки приложения
         */

        viewModel.noteList.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    noteItemAdapter.submitList(it)
                }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formatDateTimeFlow.collect {
                noteItemAdapter = MainListAdapter(it.formatDataTime.pattern, object : MainListAdapter.MainListListener {
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
                                Toast.makeText(requireContext(), R.string.edit_toast_unpinned, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            action(
                                titleResId = R.string.action_send,
                                iconResId = R.drawable.ic_share,
                                condition = this@BaseListFragment is MainListFragment
                                        || this@BaseListFragment is FavoriteFragment
                            ) {
                                startActivity(Intent.createChooser(ShareHelper.shareTextNoteItem(noteItem), "Share by"))
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
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.edit_toast_move_to_trash),
                                    Toast.LENGTH_SHORT
                                ).show()
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
            }
        }

        setupListNotes()
        val adUnitIdl = if (BuildConfig.DEBUG) {
            R.string.bannerMainListTest
        } else {
            R.string.bannerMainList
        }
        loadBannerAds(requireContext(), binding.adsFrameLayout, AdSize.FULL_BANNER, adUnitIdl)
    }

    private fun setupListNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.layoutManagerFlow.collect {
                binding.rvMainList.layoutManager = when (it.dataStoreStyleListNotes) {
                    DataStoreStyleListNotes.LIST -> {
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }

                    DataStoreStyleListNotes.GRID -> {
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    }
                }
                binding.rvMainList.adapter = noteItemAdapter
            }
        }
    }

    private fun optionMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
                menuInflater.inflate(R.menu.main_menu, menu)
                this@BaseListFragment.mainMenu = menu
                setupMenu()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.list_menu_type_layout_manager -> setNoteStyle()
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupMenu() = mainMenu?.run {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.layoutManagerFlow.collect {
                findItem(R.id.list_menu_type_layout_manager).apply {
//                    setIcon(if (it.layoutManager == TypeLayoutManager.STAGGERED_GRID_LAYOUT_MANAGER) R.drawable.ic_list_view else R.drawable.ic_grid_view)
//                    setTitle(if (it.layoutManager == TypeLayoutManager.STAGGERED_GRID_LAYOUT_MANAGER) R.string.list_menu_linear else R.string.list_menu_staggered_grid)
                }
            }
        }
    }

    private fun setNoteStyle() {
        viewLifecycleOwner.lifecycleScope.launch {
            when (viewModel.layoutManagerFlow.first().dataStoreStyleListNotes) {
                DataStoreStyleListNotes.LIST -> {
                    viewModel.onTypeLayoutManager(DataStoreStyleListNotes.GRID)
                }

                DataStoreStyleListNotes.GRID -> {
                    viewModel.onTypeLayoutManager(DataStoreStyleListNotes.LIST)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}