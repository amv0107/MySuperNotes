package com.amv.simple.app.mysupernotes.presentation.listOfNotesByCategoryOrTag

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.amv.simple.app.mysupernotes.presentation.MainActivity
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListOfNotesByCategoryOrTag : BaseListFragment() {

    private val args: ListOfNotesByCategoryOrTagArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNotesByCategory(args.categoryId)

        (requireActivity() as MainActivity).updateToolbarTitle(args.title)
    }
}