package com.amv.simple.app.mysupernotes.presentation.core

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.PartResultBinding
import com.amv.simple.app.mysupernotes.domain.util.Result
import com.amv.simple.app.mysupernotes.presentation.archiveList.ArchiveListFragment
import com.amv.simple.app.mysupernotes.presentation.favoriteList.FavoriteFragment
import com.amv.simple.app.mysupernotes.presentation.trashList.TrashFragment

fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = { binding.progressBar.visibility = View.VISIBLE },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
            binding.epmtyList.text = when (this@renderSimpleResult) {
                is ArchiveListFragment -> getString(R.string.error_empty_list_archive_screen)
                is FavoriteFragment -> getString(R.string.error_empty_list_favorite_screen)
                is TrashFragment -> getString(R.string.error_empty_list_trash_screen)
                else -> getString(R.string.error_empty_list_main_screen)
            }
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.errorContainer && it.id != R.id.progressBar }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        }
    )
}