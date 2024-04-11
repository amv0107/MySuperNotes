package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentSettingsBinding
import com.amv.simple.app.mysupernotes.presentation.MainActivity
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreFormatDateTime
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreLanguage
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel>()
    private var currentLanguageOrdinal: Int = 0
    private var currentFormatDateTimeOrdinal: Int = 0
    private var currentThemeOrdinal: Int = 0
    private var currentStyleListOrdinal: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupSelectLanguageDialog()
        setupSelectThemeDialog()
        setupSelectFormatDatetime()
        setupSelectStyleListNotes()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.language.setOnClickListener {
            showSelectLanguageDialog()
        }

        binding.theme.setOnClickListener {
            showSelectThemeDialog()
        }

        binding.formatDateTime.setOnClickListener {
            showSelectFormatDateTime()
        }

        binding.typeLayoutManager.setOnClickListener {
            showSelectStyleListNote()
        }

        observeViewModel()
    }

    private fun showSelectLanguageDialog() {
        SelectLanguageDialog.show(parentFragmentManager, currentLanguageOrdinal)
    }

    private fun setupSelectLanguageDialog() {
        SelectLanguageDialog.setupListener(parentFragmentManager, this) {
            val language = enumValues<DataStoreLanguage>()[it]
            viewModel.onTypeLanguageApp(language)
        }
    }

    private fun showSelectThemeDialog() {
        SelectThemeDialog.show(parentFragmentManager, currentThemeOrdinal)
    }

    private fun setupSelectThemeDialog() {
        SelectThemeDialog.setupListener(parentFragmentManager, this) {
            val theme = enumValues<DataStoreTypeTheme>()[it]
            viewModel.onTheme(theme)
        }
    }

    private fun showSelectFormatDateTime() {
        SelectFormatDateTimeDialog.show(parentFragmentManager, currentFormatDateTimeOrdinal)
    }

    private fun setupSelectFormatDatetime() {
        SelectFormatDateTimeDialog.setupListener(parentFragmentManager, this) {
            val format = enumValues<DataStoreFormatDateTime>()[it]
            viewModel.onTypeFormatDateTime(format)
        }
    }

    private fun showSelectStyleListNote() {
        SelectStyleListNoteDialog.show(parentFragmentManager, currentStyleListOrdinal)
    }

    private fun setupSelectStyleListNotes() {
        SelectStyleListNoteDialog.setupListener(parentFragmentManager, this) {
            val style = enumValues<DataStoreStyleListNotes>()[it]
            viewModel.onStyleListNotes(style)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.languageFlow.collectLatest {
                currentLanguageOrdinal = it.languageApp.ordinal
                binding.language.setCurrentValueText(it.languageApp.lang)
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formatDateTimeFlow.collect {
                currentFormatDateTimeOrdinal = it.formatDataTime.ordinal
                binding.formatDateTime.setCurrentValueText(it.formatDataTime.title)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.themeAppFlow.collectLatest {
                currentThemeOrdinal = it.typeTheme.ordinal
                binding.theme.setCurrentValueText(getString(it.typeTheme.title))
                (activity as MainActivity).setUIMode(it.typeTheme)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.styleListNotesFlow.collect {
                currentStyleListOrdinal = it.dataStoreStyleListNotes.ordinal
                binding.typeLayoutManager.setCurrentValueText(getString(it.dataStoreStyleListNotes.title))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}