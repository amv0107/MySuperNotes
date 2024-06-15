package com.amv.simple.app.mysupernotes.presentation.audioRecorder.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ProgressBarWaveform(
    context: Context?, attrs: AttributeSet?
): View(context, attrs) {
    private var paint = Paint()
    private var amplitude = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private var radius = 6f
    private var w = 9f
    private var d = 6f

    private var sw = 0f
    private var sh = 200f

    private var maxSpikes = 0

    init {
        paint.color = Color.rgb(244, 81, 30)

        sw = resources.displayMetrics.widthPixels.toFloat()

        maxSpikes = (sw / (w + d)).toInt()
    }

    fun addAmplitude(amp: Float) {
        val norm = (amp.toInt() / 7).coerceAtMost(400).toFloat()
        amplitude.add(norm)

        spikes.clear()
        val amps = amplitude.takeLast(maxSpikes)
        for (i in amps.indices) {
            val left = sw - i * (w + d)
            val top = sh / 2 - amps[i] / 2
            val right = left + w
            val bottom = top + amps[i]

            spikes.add(RectF(left, top, right, bottom))
        }

        invalidate()
    }

    fun clear(): ArrayList<Float> {

        val amps = amplitude.clone() as ArrayList<Float>
        amplitude.clear()
        spikes.clear()
        invalidate()

        return amps
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        spikes.forEach {
            canvas.drawRoundRect(it, radius, radius, paint)
        }
    }
}