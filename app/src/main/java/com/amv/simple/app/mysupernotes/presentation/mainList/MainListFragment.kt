package com.amv.simple.app.mysupernotes.presentation.mainList

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : BaseListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrateNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_editorFragment)
        }
        viewModel.getNoteList(TypeList.MAIN_LIST)
    }
}