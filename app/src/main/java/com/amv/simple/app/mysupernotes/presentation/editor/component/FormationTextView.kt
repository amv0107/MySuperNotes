package com.amv.simple.app.mysupernotes.presentation.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
    COLOR_TEXT,
    COLOR_TEXT_FILL,
    TEXT_SIZE_DECREASE,
    TEXT_SIZE_INCREASE
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
            binding.layout.backgroundTintList = ColorStateList.valueOf(backgroundMenu)

            val colorIcon = typedArray.getColor(R.styleable.FormationTextView_colorIcon, Color.GRAY)
            binding.btnBold.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.btnItalic.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.btnUnderlined.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.btnFormatColorText.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.btnFormatColorFill.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.btnTextDecrease.imageTintList = ColorStateList.valueOf(colorIcon)
            binding.tvSize.setTextColor(ColorStateList.valueOf(colorIcon))
            binding.btnTextIncrease.imageTintList = ColorStateList.valueOf(colorIcon)

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
            btnFormatColorText.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.COLOR_TEXT) }
            btnFormatColorFill.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.COLOR_TEXT_FILL) }
            btnTextDecrease.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.TEXT_SIZE_DECREASE) }
            btnTextIncrease.setOnClickListener { this@FormationTextView.listener?.invoke(FormationTextAction.TEXT_SIZE_INCREASE) }
        }
    }

    fun setListener(listener: OnFormationTextActionListener?) {
        this.listener = listener
    }
}