package com.amv.simple.app.mysupernotes.presentation.audioRecorder.recorder

import android.media.MediaRecorder
import java.io.IOException

class AudioRecorder(
) {
    private lateinit var recorder: MediaRecorder

    private fun createRecorder(): MediaRecorder {
        return MediaRecorder()
    }

    fun startRecording(fd: String) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(fd)

            try {
                prepare()
            } catch (_: IOException) {
            }

            start()

            recorder = this
        }
    }

    fun maxAmplitude(): Float = recorder.maxAmplitude.toFloat()

    fun pauseRecorder() = recorder.pause()

    fun resumeRecorder() = recorder.resume()

    fun stopRecorder() {
        recorder.stop()
        recorder.reset()
    }
}