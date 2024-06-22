package com.amv.simple.app.mysupernotes.presentation.audioRecorder.recorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.DialogRecorderBinding
import com.amv.simple.app.mysupernotes.presentation.core.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Permission from: https://www.youtube.com/watch?v=cGPPZqp8qis&list=PLRmiL0mct8WnodKkGLpBN0mfXIbAAX-Ux&index=36
 * AudiRecorder from: https://www.youtube.com/playlist?list=PLpZQVidZ65jPz-XIHdWi1iCra8TU9h_kU
 *
 */
@AndroidEntryPoint
class BottomSheetDialogRecorder @Inject constructor(
    private val listener: OnSaveRecordToDB
) :
    BottomSheetDialogFragment(R.layout.dialog_recorder), Timer.OnTimerTickListener {

    interface OnSaveRecordToDB {
        fun onSaveAudioRecordToDB(path: String, fileName: String)
    }

    private var _binding: DialogRecorderBinding? = null
    private val binding get() = _binding!!

    private val recordAudioPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPermissionResultForRecordAudio
    )

    private val recorder by lazy { AudioRecorder() }

    private var dirPath = ""
    private var filename = ""

    private var isRecording = false
    private var isPaused = false

    private lateinit var timer: Timer
    private lateinit var amplitudes: ArrayList<Float>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogRecorderBinding.bind(view)

        timer = Timer(this)

        binding.btnRecordPause.setOnClickListener {
            when {
                isPaused -> resumeRecorder()
                isRecording -> pauseRecorder()
                else -> recordAudioPermissionRequestLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }

        binding.btnDone.setOnClickListener {
            stopRecord()
            listener.onSaveAudioRecordToDB("", "")
            showToast("Saved record to DB")
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            if (isRecording || isPaused) {
                stopRecord()
                File("$dirPath$filename.mp3")
            }
            dismiss()
            // TODO: ConfirmDialog "Сбросить аудиозапись?" "Отмена" <-> "Сбросить"
        }

        binding.btnCancel.isClickable = true
        binding.btnDone.isClickable = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onTimerTick(duration: String) {
        binding.title.text = duration
        binding.progressbarWaveform.addAmplitude(recorder.maxAmplitude())
    }

    private fun onGotPermissionResultForRecordAudio(granted: Boolean) {
        if (granted) {
            startRecording()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO))
                askUserForOpeningAppSettings()
            else
                showToast("Запись невозможна. Разрешение отклонено")
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity?.packageName, null)
        )
        if (activity?.packageManager?.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        )
            showToast("Разрешение отклонено навсегда")
        else
            AlertDialog.Builder(requireContext())
                .setTitle("Разрешение отклонено")
                .setMessage(
                    "Вы навсегда отказались от разрешения.  " +
                            "Вы можете изменить свое решение в настройках приложения.\n\n" +
                            "Хотите открыть настройки приложения?"
                )
                .setPositiveButton("Открыть") { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
    }

    private fun pauseRecorder() {
        recorder.pauseRecorder()
        isPaused = true
        binding.btnRecordPause.setImageResource(R.drawable.ic_record)
        binding.labelBtnRecord.text = "Пауза"
        timer.pause()
    }

    private fun resumeRecorder() {
        recorder.resumeRecorder()
        isPaused = false
        binding.btnRecordPause.setImageResource(R.drawable.ic_pause)
        binding.labelBtnRecord.text = "Запись"
        timer.start()
    }

    private fun stopRecord() {
        timer.stop()
        recorder.stopRecorder()
        isPaused = false
        isRecording = false
        binding.btnCancel.isClickable = false
        binding.btnDone.isClickable = false
        binding.btnRecordPause.setImageResource(R.drawable.ic_record)
        binding.title.text = "00:00"

        amplitudes = binding.progressbarWaveform.clear()
    }

    private fun startRecording() {
        dirPath = "${requireActivity().externalCacheDir?.absolutePath}/"
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd - hh:mm:ss")
        val date = simpleDateFormat.format(Date())
        filename = "audio_record_$date"

        recorder.startRecording("$dirPath$filename.mp3")

        binding.btnRecordPause.setImageResource(R.drawable.ic_pause)
        isRecording = true
        isPaused = false

        timer.start()
        binding.btnCancel.isClickable = true
        binding.btnDone.isClickable = true
    }

    companion object {

        fun show(fragmentManager: FragmentManager, listener: OnSaveRecordToDB) {
            BottomSheetDialogRecorder(listener).show(fragmentManager, null)
        }
    }
}