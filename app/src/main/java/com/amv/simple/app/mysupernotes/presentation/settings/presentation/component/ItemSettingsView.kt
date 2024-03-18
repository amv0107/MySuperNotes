package com.amv.simple.app.mysupernotes.presentation.settings.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ItemSettingsBinding

//https://www.youtube.com/watch?v=U2bxEOqf6f0
//https://habr.com/ru/articles/727744/
class ItemSettingsView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: ItemSettingsBinding

    // View создается с помощью XML-разметки и задается значение стиля из темы
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    // View создаем с испольщованием XML-макета
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    // View создаем из кода, НЕ из XML
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_settings, this, true)
        binding = ItemSettingsBinding.bind(this)
        binding.root.isClickable = true
        binding.root.setBackgroundColor(resources.getColor(R.color.white))
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true )
        binding.root.setBackgroundResource(outValue.resourceId)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ItemSettingsView, defStyleAttr, defStyleRes)

        with(binding) {
            val title = typeArray.getString(R.styleable.ItemSettingsView_title)
            tvTitle.text = title ?: "Title"

            val visibleCurrentValue = typeArray.getBoolean(R.styleable.ItemSettingsView_visibleCurrentValue, false)
            if (visibleCurrentValue) {
                tvCurrentValue.visibility = View.VISIBLE
            }
            else {
                tvCurrentValue.visibility = View.GONE
            }

            val currentValue = typeArray.getString(R.styleable.ItemSettingsView_currentValue)
            setCurrentValueText(currentValue)

            val icon = typeArray.getDrawable(R.styleable.ItemSettingsView_icon)
            imIcon.setImageDrawable(icon)

        }

        typeArray.recycle()
    }

    fun setCurrentValueText(text: String?) {
        binding.tvCurrentValue.text = text?: "Current value"
    }
}