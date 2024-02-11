package com.amv.simple.app.mysupernotes.presentation.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amv.simple.app.mysupernotes.databinding.FragmentEditorBinding
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import dagger.hilt.android.AndroidEntryPoint

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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        binding.fabSaveNote.setOnClickListener {
            viewModel.addNoteItem(
                binding.etTitleNote.text.toString(),
                binding.etTextContentNote.text.toString()
            )
        }
    }

    private fun launchEditMode() {
        viewModel.getNoteItem(args.noteId)
        viewModel.noteItem.observe(viewLifecycleOwner) {result ->
            result.takeSuccess()?.let { item ->
                binding.apply {
                    etTitleNote.setText(item.title)
                    tvDateTimeNote.text = item.date
                    etTextContentNote.setText(item.textContent)
                }
            }
        }

        binding.fabSaveNote.setOnClickListener {
            Toast.makeText(requireContext(), "Update note item", Toast.LENGTH_SHORT).show()
        }
    }

}