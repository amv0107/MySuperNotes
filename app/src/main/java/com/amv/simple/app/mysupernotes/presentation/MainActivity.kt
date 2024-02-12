package com.amv.simple.app.mysupernotes.presentation

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
    }

    fun updateTitleToolbar(@StringRes title: Int){
        binding.toolbarTitle.setText(title)
    }

    fun updateColorToolbar(@ColorRes backgroundColor: Int){
        binding.mainToolbar.setBackgroundColor(getColor(backgroundColor))
    }
}