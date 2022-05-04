package com.example.apverse.ui.student.computer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.FirestoreClass

class SComputerViewModel(
    private val fragment: SComputerFragment
) : ViewModel() {

    init {
        loadComputers()
        Log.i("ApVerse::SCompVM", "SComputerViewModel created!")
    }

    private fun loadComputers() {
        FirestoreClass().getComputers(fragment)
    }
}