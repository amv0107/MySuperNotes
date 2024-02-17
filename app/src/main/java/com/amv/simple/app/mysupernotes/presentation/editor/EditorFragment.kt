package com.amv.simple.app.mysupernotes.presentation.editor

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
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentEditorBinding
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// Способы передачи аргумента для открытия заметки для редактирования:
// !!!!---КАКОЙ ПРАВИЛЬНО НЕЗНАЮ---!!!!
// - Передать из id и в EditorFragment загрузить из БД заметку (Пока выбрал этот способ)
// - Передать заметку как Parcelable
// - Через SharedViewModel
@AndroidEntryPoint
class EditorFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditorViewModel by viewModels()

    val args: EditorFragmentArgs by navArgs()
    private var mainMenu: Menu? = null

    private var isPin: Boolean = false
    private var isFavorite: Boolean = false
    private var isArchive: Boolean = false
    private var isDelete: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchModeScreen()
        observeViewModel()
        optionsMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun optionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
                lifecycleScope.launch {
                    menuInflater.inflate(R.menu.edit_menu, menu)
                    this@EditorFragment.mainMenu = menu
                    setupMenuItem()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.edit_menu_save_note -> saveNote()
                    R.id.edit_menu_pin -> pinNote()
                    R.id.edit_menu_favorite -> favoriteNote()
                    R.id.edit_menu_archive -> archiveNote()
                    R.id.edit_menu_share -> {}
                    R.id.edit_menu_delete -> deleteNote()
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupMenuItem() = mainMenu?.run {
        if (args.noteId != 0) this.forEach { it.isVisible = true }

        findItem(R.id.edit_menu_save_note).apply { }
        findItem(R.id.edit_menu_pin).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isPinned?.let {
                    isPin = it
                }
                setIcon(if (isPin) R.drawable.ic_un_pin else R.drawable.ic_pin)
                setTitle(if (isPin) R.string.edit_menu_unpin else R.string.edit_menu_pin)
            }
        }
        findItem(R.id.edit_menu_favorite).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isFavorite?.let {
                    isFavorite = it
                }
                setIcon(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_un_favorite)
                setTitle(if (isFavorite) R.string.edit_menu_un_favorite else R.string.edit_menu_add_favorite)
            }
        }
        findItem(R.id.edit_menu_archive).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isArchive?.let {
                    isArchive = it
                }
                isVisible = result.takeSuccess() != null && result.takeSuccess()?.isArchive == false
            }
        }
        findItem(R.id.edit_menu_share).apply { }
        findItem(R.id.edit_menu_delete).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isDelete?.let {
                    isDelete = it
                }
                isVisible = result.takeSuccess() != null && result.takeSuccess()?.isDelete == false
            }
        }
    }

    private fun pinNote() {
        viewModel.changePin()
        if (!isPin)
            Toast.makeText(requireContext(), getString(R.string.edit_toast_pinned), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(requireContext(), getString(R.string.edit_toast_unpinned), Toast.LENGTH_SHORT).show()
    }

    private fun favoriteNote() {
        viewModel.changeFavorite()
        if (!isFavorite)
            Toast.makeText(requireContext(), getString(R.string.edit_toast_un_favorite), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(requireContext(), getString(R.string.edit_toast_favorite), Toast.LENGTH_SHORT).show()
    }

    private fun archiveNote() {
        viewModel.changeArchive()
        if (!isArchive)
            Toast.makeText(requireContext(), getString(R.string.edit_toast_archive), Toast.LENGTH_SHORT).show()
    }

    private fun deleteNote() {
        viewModel.moveNoteToTrash()
        if (!isDelete)
            Toast.makeText(requireContext(), getString(R.string.edit_toast_move_to_trash), Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun launchModeScreen() {
        when (args.noteId) {
            0 -> launchAddMode()
            else -> launchEditMode()
        }
    }

    private fun saveNote(){
        if (args.noteId == 0){
            viewModel.addNoteItem(
                binding.etTitleNote.text.toString(),
                binding.etTextContentNote.text.toString(),
            )
        } else {
            viewModel.updateNoteItem(
                binding.etTitleNote.text.toString(),
                binding.etTextContentNote.text.toString()
            )
        }
    }

    private fun launchAddMode() {
        binding.tvDateTimeNote.text = TimeManager.getCurrentTime()
    }

    private fun launchEditMode() {
        viewModel.getNoteItem(args.noteId)
        viewModel.noteItem.observe(viewLifecycleOwner) { result ->
            result.takeSuccess()?.let { item ->
                binding.apply {
                    etTitleNote.setText(item.title)
                    tvDateTimeNote.text = item.date
                    etTextContentNote.setText(item.textContent)
                }
            }
        }
    }

}