package com.amv.simple.app.mysupernotes.presentation.audioRecorder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.amv.simple.app.mysupernotes.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Recorder: BottomSheetDialogFragment(R.layout.dialog_recorder) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            Recorder().show(fragmentManager, null)

        }
    }
}