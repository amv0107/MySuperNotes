package com.amv.simple.app.mysupernotes.presentation.archiveList

import android.os.Bundle
import android.view.View
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveListFragment : BaseListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNoteList(archiveNotes = true)

    }

}