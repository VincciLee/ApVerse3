package com.example.apverse.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(private var date:String = ""): DialogFragment(),
    DatePickerDialog.OnDateSetListener
{
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // default date
        var year = 0
        var month = 0
        var day = 0

        if (date == "") {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }
        else {
            year = date.substringBefore("-").toInt()
            date = date.substringAfter("-")
            month = date.substringBefore("-").toInt() - 1
            day = date.substringAfter("-").toInt()
        }

        Log.i("ApVerse::DatePicker", "Date = $year-$month-$day")

        // return new DatePickerDialog instance
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)

        val selectedDateBundle = Bundle()
        selectedDateBundle.putString("SELECTED_DATE", selectedDate)

        setFragmentResult("REQUEST_KEY", selectedDateBundle)
    }

}