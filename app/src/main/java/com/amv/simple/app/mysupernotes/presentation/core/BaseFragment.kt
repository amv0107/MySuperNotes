package com.amv.simple.app.mysupernotes.presentation.core

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.Result
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.presentation.mainList.MainListFragment

abstract class BaseFragment : Fragment() {

    fun <T> renderResult(
        root: ViewGroup, result: Result<T>,
        onPending: () -> Unit,
        onError: (Exception) -> Unit,
        onSuccess: (T) -> Unit
    ) {
        root.children
            .filter {
                it.id != R.id.banner && (if (this is MainListFragment) it.id != R.id.fabCrateNote else true)
            }
            .forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }
}