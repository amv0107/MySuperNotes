package com.amv.simple.app.mysupernotes.presentation.trashList

import android.os.Bundle
import android.view.View
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrashFragment : BaseListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNoteList(TypeList.DELETE_LIST)

    }

}