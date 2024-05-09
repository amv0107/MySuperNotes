package com.amv.simple.app.mysupernotes.presentation.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Layout
import android.text.TextWatcher
import android.text.style.AlignmentSpan
import android.text.style.BulletSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentEditorBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.util.ShareHelper
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import com.amv.simple.app.mysupernotes.presentation.editor.component.FormationParagraphAlignAction
import com.amv.simple.app.mysupernotes.presentation.editor.component.FormationTextAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TAG"

// Способы передачи аргумента для открытия заметки для редактирования:
// !!!!---КАКОЙ ПРАВИЛЬНО НЕЗНАЮ---!!!!
// - Передать из id и в EditorFragment загрузить из БД заметку (Пока выбрал этот способ)
// - Передать заметку как Parcelable
// - Через SharedViewModel

private enum class WhatWePaint {
    FOREGROUND,
    BACKGROUND
}

@AndroidEntryPoint
class EditorFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteItem: NoteItem
    private val viewModel: EditorViewModel by viewModels()

    val args: EditorFragmentArgs by navArgs()
    private var mainMenu: Menu? = null

    private var isPin: Boolean = false
    private var isFavorite: Boolean = false
    private var isArchive: Boolean = false
    private var isDelete: Boolean = false
    private lateinit var whatWePaint: WhatWePaint

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
        titleFocusListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.etTitleNote.text?.isEmpty() == true && binding.etTextContentNote.text.isEmpty())
                    findNavController().navigateUp()
                else
                    saveNote()
            }
        })

        actionMenuCallback()

        listeners()
    }

    private fun listeners() {
        binding.selectColorPicker.setListener { color ->
            val startPos = binding.etTextContentNote.selectionStart
            val endPos = binding.etTextContentNote.selectionEnd
            val editText = binding.etTextContentNote
            when (whatWePaint) {
                WhatWePaint.FOREGROUND -> FormationText.foregroundColorText(startPos, endPos, color, editText)
                WhatWePaint.BACKGROUND -> FormationText.backgroundColorText(startPos, endPos, color, editText)
            }
        }

        binding.formationMenu.setListener { action ->
            val startPos = binding.etTextContentNote.selectionStart
            val endPos = binding.etTextContentNote.selectionEnd
            val editText = binding.etTextContentNote
            val color = ContextCompat.getColor(requireContext(), R.color.grey)

            when (action) {
                FormationTextAction.BOLD -> FormationText.bold(startPos, endPos, editText)
                FormationTextAction.ITALIC -> FormationText.italic(startPos, endPos, editText)
                FormationTextAction.UNDERLINE -> FormationText.underline(startPos, endPos, editText)
                FormationTextAction.ALIGN -> showFormationParagraphAlignMenu()
                FormationTextAction.BULLET -> FormationParagraph.bulletSpan(startPos, endPos, editText, color)
                FormationTextAction.COLOR_TEXT -> showColorPicker(WhatWePaint.FOREGROUND, startPos, endPos, editText)
                FormationTextAction.COLOR_TEXT_FILL -> showColorPicker(
                    WhatWePaint.BACKGROUND,
                    startPos,
                    endPos,
                    editText
                )

                FormationTextAction.TEXT_SIZE -> FormationText.sizeText(
                    startPos = startPos,
                    endPos = endPos,
                    size = binding.formationMenu.sizeText,
                    view = editText
                )
            }
        }

        binding.formationAlignMenu.setListener { action ->
            val startPos = binding.etTextContentNote.selectionStart
            val endPos = binding.etTextContentNote.selectionEnd
            val editText = binding.etTextContentNote

            when (action) {
                FormationParagraphAlignAction.LEFT -> FormationParagraph.alignLeft(startPos, endPos, editText)
                FormationParagraphAlignAction.CENTER -> FormationParagraph.alignCenter(startPos, endPos, editText)
                FormationParagraphAlignAction.RIGHT -> FormationParagraph.alignRight(startPos, endPos, editText)
            }
        }
    }

    private fun showColorPicker(whatWePaint: WhatWePaint, startPos: Int, endPos: Int, view: EditText) {
        if (this::whatWePaint.isInitialized && this.whatWePaint == whatWePaint && binding.selectColorPicker.isVisible) {
            binding.selectColorPicker.visibility = View.GONE
            binding.formationMenu.isSelectedForeground = false
            binding.formationMenu.isSelectedBackground = false
        } else {
            if (binding.selectColorPicker.isVisible) {
                binding.selectColorPicker.visibility = View.GONE
                binding.formationMenu.isSelectedForeground = false
                binding.formationMenu.isSelectedBackground = false
            }
            this.whatWePaint = whatWePaint
            binding.formationAlignMenu.visibility = View.GONE
            when (whatWePaint) {
                WhatWePaint.FOREGROUND -> {
                    binding.selectColorPicker.currentColor =
                        FormationText.getForegroundColorText(startPos, endPos, view)
                    binding.selectColorPicker.listColor =
                        requireContext().resources.getStringArray(R.array.color_palette_foreground)
                    binding.formationMenu.isSelectedForeground = true
                }

                WhatWePaint.BACKGROUND -> {
                    binding.selectColorPicker.currentColor =
                        FormationText.getBackgroundColorText(startPos, endPos, view)
                    binding.selectColorPicker.listColor =
                        requireContext().resources.getStringArray(R.array.color_palette_background)
                    binding.formationMenu.isSelectedBackground = true
                }
            }
            binding.selectColorPicker.visibility = View.VISIBLE
        }
    }

    private fun showFormationParagraphAlignMenu() {
        binding.formationAlignMenu.visibility = if (binding.formationAlignMenu.isVisible) {
            View.GONE
        } else {
            binding.selectColorPicker.visibility = View.GONE
            View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun actionMenuCallback() {
        binding.etTextContentNote.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                hideKeyboard()
                binding.formationMenu.visibility = View.VISIBLE
                setStateButtonMenuFormation()
                setStateButtonAlign()
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, menu: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                binding.formationAlignMenu.visibility = View.GONE
                binding.selectColorPicker.visibility = View.GONE
                binding.formationMenu.visibility = View.GONE
            }
        }
    }

    private fun setStateButtonAlign() {
        val startPos = binding.etTextContentNote.selectionStart
        val endPos = binding.etTextContentNote.selectionEnd

        val stateAlign = binding.etTextContentNote.text.getSpans(
            startPos,
            endPos,
            AlignmentSpan::class.java
        )

        val leftState = stateAlign.any { it.alignment == Layout.Alignment.ALIGN_NORMAL }
        val centerState = stateAlign.any { it.alignment == Layout.Alignment.ALIGN_CENTER }
        val rightState = stateAlign.any { it.alignment == Layout.Alignment.ALIGN_OPPOSITE }

        binding.formationAlignMenu.setBackgroundLeftBtn(leftState)
        binding.formationAlignMenu.setBackgroundCenterBtn(centerState)
        binding.formationAlignMenu.setBackgroundRightBtn(rightState)
    }

    private fun setStateButtonMenuFormation() {
        val startPos = binding.etTextContentNote.selectionStart
        val endPos = binding.etTextContentNote.selectionEnd

        val stateStyleSpan = binding.etTextContentNote.text.getSpans(
            startPos,
            endPos,
            StyleSpan::class.java
        )

        val boldSate = stateStyleSpan.any { it.style == Typeface.BOLD }
        binding.formationMenu.setBackgroundBoldBtn(boldSate)

        val italicSate = stateStyleSpan.any { it.style == Typeface.ITALIC }
        binding.formationMenu.setBackgroundItalicBtn(italicSate)

        binding.formationMenu.setBackgroundUnderlineBtn(
            binding.etTextContentNote.text.getSpans(
                startPos,
                endPos,
                UnderlineSpan::class.java
            ).isNotEmpty()
        )

        binding.formationMenu.setBackgroundBulletBtn(
            binding.etTextContentNote.text.getSpans(
                startPos,
                endPos,
                BulletSpan::class.java
            ).isNotEmpty()
        )
    }

    private fun hideKeyboard() {
        try {
            val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etTextContentNote.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun titleFocusListener() {
        binding.etTitleNote.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.titleContainer.helperText = validTitle()
        }

        binding.etTitleNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.titleContainer.helperText = validTitle()
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun validTitle(): String? {
        val titleText = binding.etTitleNote.text.toString()
        if (titleText.length > 50)
            return resources.getString(R.string.edit_text_helper_error_title)

        return null

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
                    R.id.edit_menu_share -> shareNote()
                    R.id.edit_menu_delete -> deleteNote()
                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun setupMenuItem() = mainMenu?.run {
        if (args.noteId != 0) this.forEach { it.isVisible = true }

        findItem(R.id.edit_menu_pin).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isPinned?.let {
                    isPin = it
                }
                setIcon(if (isPin) R.drawable.ic_un_pin else R.drawable.ic_pin)
                setTitle(if (isPin) R.string.edit_menu_unpin else R.string.edit_menu_pin)

                isVisible = result.takeSuccess()?.isDelete == false
            }
        }
        findItem(R.id.edit_menu_favorite).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isFavorite?.let {
                    isFavorite = it
                }
                setIcon(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_un_favorite)
                setTitle(if (isFavorite) R.string.edit_menu_un_favorite else R.string.edit_menu_add_favorite)

                isVisible = result.takeSuccess()?.isDelete == false
            }
        }
        findItem(R.id.edit_menu_archive).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                result.takeSuccess()?.isArchive?.let {
                    isArchive = it
                }

                isVisible =
                    result.takeSuccess() != null && result.takeSuccess()?.isArchive == false && result.takeSuccess()?.isDelete == false
            }
        }
        findItem(R.id.edit_menu_share).apply {
            viewModel.noteItem.observe(viewLifecycleOwner) { result ->
                isVisible = result.takeSuccess()?.isDelete == false
            }
        }
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

    private fun shareNote() = startActivity(Intent.createChooser(ShareHelper.shareTextNoteItem(noteItem), "Share by"))

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

    private fun saveNote() {
        binding.titleContainer.helperText = validTitle()
        val validTitle = binding.titleContainer.helperText == null

        if (validTitle) {
            if (args.noteId == 0) {
                viewModel.addNoteItem(
                    binding.etTitleNote.text.toString(),
                    Html.toHtml(binding.etTextContentNote.text, Html.FROM_HTML_MODE_COMPACT),
                )
            } else {
                viewModel.updateNoteItem(
                    binding.etTitleNote.text.toString(),
                    Html.toHtml(binding.etTextContentNote.text, Html.FROM_HTML_MODE_COMPACT),
                )
            }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.edit_toast_action_short_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun launchAddMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formatDataTimeFlow.collect {
                binding.tvDateTimeNote.text = TimeManager.getTimeFormat(
                    TimeManager.getCurrentTimeToDB(),
                    it.formatDataTime.pattern
                )
            }
        }
    }

    private fun launchEditMode() {
        viewModel.getNoteItem(args.noteId)

        viewModel.noteItem.observe(viewLifecycleOwner) { result ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.formatDataTimeFlow.collect {
                    result.takeSuccess()?.let { item ->
                        noteItem = item
                        binding.apply {
                            etTitleNote.setText(item.title)
                            tvDateTimeNote.text = TimeManager.getTimeFormat(item.date, it.formatDataTime.pattern)
                            etTextContentNote.setText(Html.fromHtml(item.textContent, Html.FROM_HTML_MODE_COMPACT))

                            etTextContentNote.setReadOnly(item.isDelete) {
                                viewModel.restoreDelete(item)
                            }
                        }
                    }
                }
            }
        }
    }

}
