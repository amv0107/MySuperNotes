package com.amv.simple.app.mysupernotes.presentation.audioRecorder.components

import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.amv.simple.app.mysupernotes.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AudioPlayer: BottomSheetDialogFragment(R.layout.dialog_audio_player) {

    companion object {
        const val FILE_PATH = "FILE_PATH"
        const val FILE_NAME = "FILE_NAME"
        fun show(fragmentManager: FragmentManager, filePath: String, fileName: String) {
            AudioPlayer().apply {
                arguments = bundleOf(
                    FILE_PATH to filePath,
                    FILE_NAME to fileName
                )
                show(fragmentManager, null)
            }

        }
    }
}