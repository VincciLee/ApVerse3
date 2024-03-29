package com.example.apverse.ui.student.room

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.NewRoomBooking
import com.example.apverse.model.RoomBooking
import com.example.apverse.model.Users
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SRoomBookViewModel: ViewModel() {

    var userInfo: Users = Users()
    var userTp: String = ""
    var userNum: String = ""
    var existingBookings: ArrayList<RoomBooking> = ArrayList()
    var hasBooked: Boolean = false

    var errorMessage: String = ""

    suspend fun getUserInfo() {
        val data = Firestore().getUserInfo()
        for (i in data?.documents!!){
            userInfo = i?.toObject(Users::class.java)!!
        }

        userTp = userInfo.user_email.substring(0, 2).uppercase()
        userNum = userInfo.user_email.substringBefore("@").substringAfter("tp")
    }

    suspend fun validateRoomBooking(roomBooking: NewRoomBooking) : Boolean {
        existingBookings.clear()

        val roomId = roomBooking.room_id
        val bookingDate = roomBooking.date
        val time = roomBooking.time
        val hour = time.substringBefore(":")
        val minute = time.substringAfter(":")

        val calendar: Calendar = Calendar.getInstance()
        val cal = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val timeFormatter = SimpleDateFormat("HH:mm")

        val currentDate = dateFormatter.format(cal)
        val currentTime = timeFormatter.format(cal)
        val currentHour = currentTime.substringBefore(":")
        val currentMinute = currentTime.substringAfter(":")

        val date = dateFormatter.parse(bookingDate)
        calendar.setTime(date)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val data = Firestore().getSelectedRoomBookings(roomId, bookingDate)
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            booking.doc_id = i.id

            if (booking.student_email != Firestore().getCurrentUserEmail()) {
                existingBookings.add(booking)
            }
            else {
                hasBooked = true
            }
        }

        if (bookingDate?.compareTo(currentDate)!! < 0){
            errorMessage = "Cannot book discussion room before today."
            return false
        }

        if (dayOfWeek == 1 || dayOfWeek == 7) {
            errorMessage = "Cannot book discussion room on weekends."
            return false
        }

        if (hour.toInt() < 9 || hour.toInt() >= 18){
            errorMessage = "The time selected must between 9am and 6pm."
            return false
        }

        if (bookingDate?.compareTo(currentDate) == 0){
            if (hour.toInt() < currentHour.toInt()) {
                errorMessage = "The time selected must later than current time."
                return false
            }
            else if(hour.toInt() == currentHour.toInt() && minute.toInt() < currentMinute.toInt()) {
                errorMessage = "The time selected must later than current time."
                return false
            }
        }

        if (minute != "30" && minute != "00") {
            errorMessage = "The time selected must 30 minutes interval."
            return false
        }

        if (hasBooked) {
            errorMessage = "You have booked this room on this day."
            return false
        }

        for (i in existingBookings) {
            val dbHour = i.time.substringBefore(":")
            val dbMinute = i.time.substringAfter(":")

            if (hour.toInt() == dbHour.toInt()) {
                errorMessage = "The time selected has been booked by other students."
                return false
            }
            else if (hour.toInt() == dbHour.toInt()-1 && minute.toInt() >= dbMinute.toInt()) {
                errorMessage = "The time selected has been booked by other students."
                return false
            }
            else if (hour.toInt() == dbHour.toInt()+1 && minute.toInt() <= dbMinute.toInt()) {
                errorMessage = "The time selected has been booked by other students."
                return false
            }
        }

        return createRoomBooking(roomBooking)
    }

    suspend fun createRoomBooking(roomBooking: NewRoomBooking) : Boolean {
        val result = Firestore().createRoomBooking(roomBooking)

        if(!result) {
            errorMessage = "Unable to book the room."
        }

        return result
    }
}