package com.example.apverse.ui.librarian.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LHomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is librarian Home Fragment"
    }
    val text: LiveData<String> = _text
}