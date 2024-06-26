package com.amv.simple.app.mysupernotes.presentation.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.indices
import androidx.core.view.size
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.MenuColorPickerBinding
import com.google.android.material.card.MaterialCardView

/**
 * Used: https://nikhilhere.medium.com/custom-color-picker-using-radio-buttons-32d31fe4605c
 */

typealias OnFormationForegroundColorListener = (color: Int) -> Unit

class ColorPickerView(
    context: Context,
    attrsSet: AttributeSet?,
    defStyleAttr: Int
) : MaterialCardView(
    context,
    attrsSet,
    defStyleAttr
), RadioGroup.OnCheckedChangeListener {

    private val binding: MenuColorPickerBinding
    private var listener: OnFormationForegroundColorListener? = null

    var listColor: Array<String> = resources.getStringArray(R.array.color_palette_foreground)
        set(value) {
            field = value
            createColorPalette(field)
        }

    var currentColor: String = ""

    constructor(context: Context, attrsSet: AttributeSet?) : this(context, attrsSet, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.menu_color_picker, this, true)
        binding = MenuColorPickerBinding.bind(this)
        binding.rgColorPicker.removeAllViews()
        initializeAttributes(attrsSet, defStyleAttr)
        initializeListeners()
    }

    override fun onCheckedChanged(radioGroup: RadioGroup, index: Int) {
        listener?.invoke(Color.parseColor(listColor[index]))
    }

    private fun initializeAttributes(attrsSet: AttributeSet?, defStyleAttr: Int) {
        if (attrsSet == null) return
        val typedArray = context.obtainStyledAttributes(attrsSet, R.styleable.ColorPickerView, defStyleAttr, 0)

        with(binding) {
            val backgroundMenu = typedArray.getColor(R.styleable.ColorPickerView_backgroundColorPickerMenu, Color.WHITE)
            layout.setBackgroundColor(backgroundMenu)
        }

        typedArray.recycle()
    }

    private fun createColorPalette(listColor: Array<String>) {
        binding.rgColorPicker.removeAllViews()
        for (i in listColor.indices) {
            //create radio button by inflating radio button layout
            val inflater = LayoutInflater.from(context)
            val rbView = inflater.inflate(R.layout.custom_radio_button, null)
            val rb = rbView.rootView as RadioButton

            //set unique id
            rb.id = i
            //set some margin to radio buttons
            val params =
                RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(6, 6, 6, 6)
            rb.layoutParams = params

            //set color from palette
            // TODO: Без фона добавлять в конце через свойство, если свойство включено то добавлять отдельную кнопку 
            if (listColor[i] == "#FFFFFFFF")
                rb.background = resources.getDrawable(R.drawable.ic_none)
            else
                rb.backgroundTintList = ColorStateList.valueOf(Color.parseColor(listColor[i]))

            rb.isChecked = listColor[i] == currentColor

            //add view
            binding.rgColorPicker.addView(rb)
        }
    }

    private fun initializeListeners() {
        binding.rgColorPicker.setOnCheckedChangeListener(this)
    }

    fun setListener(listener: OnFormationForegroundColorListener?) {
        this.listener = listener
    }

}