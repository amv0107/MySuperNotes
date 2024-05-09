package com.amv.simple.app.mysupernotes.presentation.core.sorting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.DialogSortingBinding
import com.amv.simple.app.mysupernotes.domain.util.NoteOrder
import com.amv.simple.app.mysupernotes.domain.util.OrderType

class SortingDialog : DialogFragment(R.layout.dialog_sorting) {

    private var _binding: DialogSortingBinding? = null
    val binding get() = _binding!!

    private val noteOrder: NoteOrder
        get() = requireArguments().getSerializable(ARG_NOTE_ORDER) as NoteOrder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogSortingBinding.bind(view)

        setCheckRadioGroup(noteOrder, noteOrder.orderType)
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setCheckRadioGroup(
        sortBy: NoteOrder,
        sortOrder: OrderType
    ) {
        when (sortBy) {
            is NoteOrder.Title -> binding.rgSortBy.check(R.id.rbSortByTitle)
            is NoteOrder.DateCreate -> binding.rgSortBy.check(R.id.rbSortByDateCreate)
        }

        when (sortOrder) {
            is OrderType.Ascending -> binding.rgSortOrder.check(R.id.rbSortOrderAsc)
            is OrderType.Descending -> binding.rgSortOrder.check(R.id.rbSortOrderDesc)
        }
    }

    private fun setupListeners() {
        var newNoteOrder: NoteOrder = noteOrder

        binding.rgSortBy.setOnCheckedChangeListener { _, checkedId ->
            //TODO: Кажется не переключается, точне переключаетмя после выбора порядка сортировки,
            // возможно когда будет загружаться из настроек то будет все хорошо
            when (checkedId) {
                R.id.rbSortByTitle -> newNoteOrder = NoteOrder.Title(noteOrder.orderType)
                R.id.rbSortByDateCreate -> newNoteOrder = NoteOrder.DateCreate(noteOrder.orderType)
            }
        }

        binding.rgSortOrder.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSortOrderAsc -> newNoteOrder = newNoteOrder.copy(OrderType.Ascending)
                R.id.rbSortOrderDesc -> newNoteOrder = newNoteOrder.copy(OrderType.Ascending)
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnApply.setOnClickListener {
            Log.d(
                "TAG",
                "DialogSetListener: ${noteOrder.javaClass.simpleName}+${noteOrder.orderType.javaClass.simpleName}"
            )
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_NOTE_ORDER_RESPONSE to newNoteOrder))
            dismiss()
        }
    }

    companion object {
        @JvmStatic // Не уверен что здесь это нужно
        private val TAG = SortingDialog::class.java.simpleName

        @JvmStatic
        private val KEY_NOTE_ORDER_RESPONSE = "KEY_NOTE_ORDER_RESPONSE"

        @JvmStatic
        private val ARG_NOTE_ORDER = "ARG_NOTE_ORDER"

        @JvmStatic // Не уверен что здесь это нужно
        private val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, noteOrder: NoteOrder) {
            val dialogFragment = SortingDialog()
            dialogFragment.arguments = bundleOf(ARG_NOTE_ORDER to noteOrder)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (noteOrder: NoteOrder) -> Unit
        ) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getSerializable(KEY_NOTE_ORDER_RESPONSE) as NoteOrder)
            }
        }
    }
}