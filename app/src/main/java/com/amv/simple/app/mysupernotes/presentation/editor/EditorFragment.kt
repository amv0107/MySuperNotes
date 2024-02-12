package com.amv.simple.app.mysupernotes.presentation.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                lifecycleScope.launch {
                    viewModel.noteItem.value.let {
                        menuInflater.inflate(R.menu.edit_menu, menu)
                        if (it != null) {
                            this@EditorFragment.mainMenu = menu
                            setupMenuItem()
                        }
                    }
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
        findItem(R.id.edit_menu_pin)?.apply {
//            editViewModel.isPined.observe(viewLifecycleOwner) {
//                pinState = it
//                setIcon(if (it) R.drawable.ic_pin_filled else R.drawable.ic_pin)
//                setTitle(if (it) R.string.edit_menu_unpin else R.string.edit_menu_pin)
//            }
        }
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