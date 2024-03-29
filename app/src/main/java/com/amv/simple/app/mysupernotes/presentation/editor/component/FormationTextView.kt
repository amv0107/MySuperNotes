package com.amv.simple.app.mysupernotes.presentation.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.MenuFormationTextBinding
import com.google.android.material.card.MaterialCardView

enum class FormationTextAction {
    BOLD,
    ITALIC,
    UNDERLINE,
    ALIGN,
    BULLET,
    COLOR_TEXT,
    COLOR_TEXT_FILL,
    TEXT_SIZE,
}

typealias OnFormationTextActionListener = (FormationTextAction) -> Unit

class FormationTextView(
    context: Context,
    attrsSet: AttributeSet?,
    defStyleAttr: Int
) : MaterialCardView(
    context,
    attrsSet,
    defStyleAttr
) {
    private val binding: MenuFormationTextBinding
    private var listener: OnFormationTextActionListener? = null
    var sizeText: Int = 18

    constructor(context: Context, attrsSet: AttributeSet?) : this(context, attrsSet, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.menu_formation_text, this, true)
        binding = MenuFormationTextBinding.bind(this)
        initializeAttributes(attrsSet, defStyleAttr)
        initializeListeners()
    }

    private fun initializeAttributes(attrsSet: AttributeSet?, defStyleAttr: Int) {
        if (attrsSet == null) return
        val typedArray = context.obtainStyledAttributes(attrsSet, R.styleable.FormationTextView, defStyleAttr, 0)

        with(binding) {
            val backgroundMenu = typedArray.getColor(R.styleable.FormationTextView_backgroundMenu, Color.WHITE)
            layout.backgroundTintList = ColorStateList.valueOf(backgroundMenu)

            val colorIcon = typedArray.getColor(R.styleable.FormationTextView_colorIcon, Color.GRAY)
            btnBold.imageTintList = ColorStateList.valueOf(colorIcon)
            btnItalic.imageTintList = ColorStateList.valueOf(colorIcon)
            btnUnderlined.imageTintList = ColorStateList.valueOf(colorIcon)
            btnAlign.imageTintList = ColorStateList.valueOf(colorIcon)
            btnBulletList.imageTintList = ColorStateList.valueOf(colorIcon)
            btnFormatForegroundText.imageTintList = ColorStateList.valueOf(colorIcon)
            btnFormatBackgroundText.imageTintList = ColorStateList.valueOf(colorIcon)
            btnTextDecrease.imageTintList = ColorStateList.valueOf(colorIcon)
            tvSize.setTextColor(ColorStateList.valueOf(colorIcon))
            btnTextIncrease.imageTintList = ColorStateList.valueOf(colorIcon)

//            val currentSize = typedArray.getString(R.styleable.)
        }

        typedArray.recycle()
    }

    private fun initializeListeners() {
        with(binding) {
            btnBold.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.BOLD) }
            btnItalic.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.ITALIC) }
            btnUnderlined.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.UNDERLINE) }
            btnAlign.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.ALIGN) }
            btnBulletList.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.BULLET) }
            btnFormatForegroundText.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.COLOR_TEXT) }
            btnFormatBackgroundText.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.COLOR_TEXT_FILL) }
            tvSize.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    sizeText = tvSize.text.toString().toInt()
                    this@FormationTextView.listener?.invoke(FormationTextAction.TEXT_SIZE)
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            btnTextDecrease.setOnClickListener { binding.tvSize.text = decreaseSize().toString() }
            btnTextIncrease.setOnClickListener { binding.tvSize.text = increaseSize().toString() }
        }
    }

    private fun decreaseSize(): Int {
        var size = binding.tvSize.text.toString().toInt()
        size -= 2
        return size
    }

    private fun increaseSize(): Int {
        var size = binding.tvSize.text.toString().toInt()
        size += 2
        return size
    }

    fun setBackgroundBoldBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnBold.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnBold.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundItalicBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnItalic.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnItalic.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundAlignBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnAlign.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnAlign.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundBulletBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnBulletList.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnBulletList.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundForegroundBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnFormatForegroundText.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnFormatForegroundText.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundBackgroundBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnFormatBackgroundText.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnFormatBackgroundText.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setBackgroundUnderlineBtn(isSelected: Boolean) {
        if (isSelected)
            binding.btnUnderlined.setBackgroundColor(resources.getColor(R.color.orange_10))
        else
            binding.btnUnderlined.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun setListener(listener: OnFormationTextActionListener?) {
        this.listener = listener
    }
}