package com.amv.simple.app.mysupernotes.presentation.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
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
import com.amv.simple.app.mysupernotes.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Способы передачи аргумента для открытия заметки для редактирования:
// !!!!---КАКОЙ ПРАВИЛЬНО НЕЗНАЮ---!!!!
// - Передать из id и в EditorFragment загрузить из БД заметку (Пока выбрал этот способ)
// - Передать заметку как Parcelable
// - Через SharedViewModel
@AndroidEntryPoint
class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditorViewModel by viewModels()

    val args: EditorFragmentArgs by navArgs()
    private var mainMenu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).updateColorToolbar(R.color.yellow)
        (activity as MainActivity).updateTitleToolbar(R.string.blankTitle)
        (activity as MainActivity).window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.yellow)
        launchModeScreen()
        observeViewModel()
        optionsMenu()
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).updateColorToolbar(R.color.white)
        (activity as MainActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        (activity as MainActivity).updateTitleToolbar(R.string.app_name)
        _binding = null
    }

    private fun optionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                lifecycleScope.launch {
                    menuInflater.inflate(R.menu.edit_menu, menu)
                    this@EditorFragment.mainMenu = menu
                    setupMenuItem()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.edit_menu_save_note -> {}
                    R.id.edit_menu_pin -> {}
                    R.id.edit_menu_favorite -> {}
                    R.id.edit_menu_archive -> {}
                    R.id.edit_menu_share -> {}
                    R.id.edit_menu_delete -> {}
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupMenuItem() = mainMenu?.run {
        if (args.noteId != 0) this.forEach { it.isVisible = true }

        findItem(R.id.edit_menu_save_note).apply { }
        findItem(R.id.edit_menu_pin).apply {
            //            editViewModel.isPined.observe(viewLifecycleOwner) {
//                pinState = it
//                setIcon(if (it) R.drawable.ic_pin_filled else R.drawable.ic_pin)
//                setTitle(if (it) R.string.edit_menu_unpin else R.string.edit_menu_pin)
//            }
        }
        findItem(R.id.edit_menu_favorite).apply { }
        findItem(R.id.edit_menu_archive).apply { }
        findItem(R.id.edit_menu_share).apply { }
        findItem(R.id.edit_menu_delete).apply { }
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

    private fun launchAddMode() {
        binding.apply {
            tvDateTimeNote.text = TimeManager.getCurrentTime()
            fabSaveNote.setOnClickListener {
                viewModel.addNoteItem(
                    etTitleNote.text.toString(),
                    etTextContentNote.text.toString(),
                )
            }
        }
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

        binding.fabSaveNote.setOnClickListener {
            viewModel.updateNoteItem(
                binding.etTitleNote.text.toString(),
                binding.etTextContentNote.text.toString()
            )
        }
    }

}