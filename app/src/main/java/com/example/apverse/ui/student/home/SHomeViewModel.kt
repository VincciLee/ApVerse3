package com.example.apverse.ui.student.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SHomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is student Home Fragment"
    }
    val text: LiveData<String> = _text
}