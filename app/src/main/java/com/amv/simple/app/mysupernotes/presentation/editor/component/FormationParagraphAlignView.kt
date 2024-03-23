package com.amv.simple.app.mysupernotes.presentation.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.MenuAlignParagraphBinding
import com.google.android.material.card.MaterialCardView

enum class FormationParagraphAlignAction {
    LEFT,
    CENTER,
    RIGHT
}

typealias OnFormationParagraphAlignActionListener = (FormationParagraphAlignAction) -> Unit

class FormationParagraphAlignView(
    context: Context,
    attrsSet: AttributeSet?,
    defStyleAttr: Int
) : MaterialCardView(
    context,
    attrsSet,
    defStyleAttr
) {
    private val binding: MenuAlignParagraphBinding
    private var listener: OnFormationParagraphAlignActionListener? = null

    constructor(context: Context, attrsSet: AttributeSet?) : this(context, attrsSet, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.menu_align_paragraph, this, true)
        binding = MenuAlignParagraphBinding.bind(this)
        initializeAttributes(attrsSet, defStyleAttr)
        initializeListeners()
    }

    private fun initializeAttributes(attrsSet: AttributeSet?, defStyleAttr: Int) {
        if (attrsSet == null) return

        val typedArray = context.obtainStyledAttributes(attrsSet, R.styleable.FormationParagraphAlignView, defStyleAttr, 0)

        with(binding) {
            val backgroundMenu = typedArray.getColor(R.styleable.FormationParagraphAlignView_backgroundAlignMenu, Color.WHITE)
            layout.backgroundTintList = ColorStateList.valueOf(backgroundMenu)

            val colorIcon = typedArray.getColor(R.styleable.FormationParagraphAlignView_colorAlignIcon, Color.GRAY)
            btnLeft.imageTintList = ColorStateList.valueOf(colorIcon)
            btnCenter.imageTintList = ColorStateList.valueOf(colorIcon)
            btnRight.imageTintList = ColorStateList.valueOf(colorIcon)
        }

        typedArray.recycle()
    }

    private fun initializeListeners() {
        with(binding) {
            btnLeft.setOnClickListener { this@FormationParagraphAlignView.listener?.invoke(FormationParagraphAlignAction.LEFT) }
            btnCenter.setOnClickListener { this@FormationParagraphAlignView.listener?.invoke(FormationParagraphAlignAction.CENTER) }
            btnRight.setOnClickListener { this@FormationParagraphAlignView.listener?.invoke(FormationParagraphAlignAction.RIGHT) }
        }
    }

    fun setListener(listener: OnFormationParagraphAlignActionListener?) {
        this.listener = listener
    }
}