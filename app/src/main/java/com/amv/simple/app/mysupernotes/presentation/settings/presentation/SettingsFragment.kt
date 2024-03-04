package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupSelectLanguageDialog()
        setupSelectThemeDialog()
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
            Toast.makeText(requireContext(), "Show dialog with formatDate", Toast.LENGTH_SHORT).show()
        }

        binding.formatDateTime.setCurrentValueText("2024/02/29 - 12:33")
    }

    private fun showSelectLanguageDialog() {
        val currentLanguage = 1
        SelectLanguageDialog.show(parentFragmentManager, currentLanguage)
    }

    private fun setupSelectLanguageDialog() {
        SelectLanguageDialog.setupListener(parentFragmentManager, this) {
            Toast.makeText(requireContext(), "selected: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSelectThemeDialog() {
        val currentTheme = 1
        SelectThemeDialog.show(parentFragmentManager, currentTheme)
    }

    private fun setupSelectThemeDialog() {
        SelectThemeDialog.setupListener(parentFragmentManager, this) {
            Toast.makeText(requireContext(), "selected: $it", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}