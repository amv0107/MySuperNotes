package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        
        binding.language.setOnClickListener {
            Toast.makeText(requireContext(), "Show dialog with language", Toast.LENGTH_SHORT).show()
        }

        binding.formatDateTime.setCurrentValueText("2024/02/29 - 12:33")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}