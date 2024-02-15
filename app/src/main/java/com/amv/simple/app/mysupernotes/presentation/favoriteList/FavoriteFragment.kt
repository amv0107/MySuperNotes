package com.amv.simple.app.mysupernotes.presentation.favoriteList

import android.os.Bundle
import android.view.View
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCrateNote.visibility = View.GONE
        viewModel.getNoteList(favoriteNotes = true)
    }

}