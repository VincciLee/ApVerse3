package com.example.apverse.ui.student.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.NewRoomBooking
import com.example.apverse.model.RoomBooking
import com.example.apverse.model.Rooms
import com.example.apverse.utils.Constants
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SMyRoomViewModel: ViewModel() {

    var myRoomDetails: ArrayList<Rooms> = ArrayList()
    var myBookingInfo: RoomBooking = RoomBooking()
    var existingBookings: ArrayList<RoomBooking> = ArrayList()

    var errorMessage: String = ""
    var allBookingList: ArrayList<RoomBooking> = ArrayList()

    suspend fun getMyRoomDetails(roomId: String) {
        myRoomDetails.clear()

        val data = Firestore().getRoomDetails(roomId)
        for (i in data?.documents!!){
            val room = i?.toObject(Rooms::class.java)!!
            myRoomDetails.add(room)
        }
    }

    suspend fun getMyRoomBookingInfo(docId: String) {
        val data = Firestore().getMyRoomBookingInfo(docId)
        myBookingInfo = data?.toObject(RoomBooking::class.java)!!
        Log.i("ApVerse::SMyRoomVM", "getMyRoomBookingInfo() = Room "+myBookingInfo.room_id)
    }

    suspend fun validateRoomBooking(docId: String, roomId: String, hashMap: HashMap<String, Any>) : Boolean {
        Log.i("ApVerse::SRoomBookVM", "validateRoomBooking()")
        existingBookings.clear()

        val bookingDate = hashMap[Constants.DATE].toString()
        val time = hashMap[Constants.TIME].toString()
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

        Log.i("ApVerse::SRoomBookVM", "bookingDate = $bookingDate, minute = $minute")
        val date = dateFormatter.parse(bookingDate)
        Log.i("ApVerse::SRoomBookVM", "date = $date")
        calendar.setTime(date)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        Log.i("ApVerse::SRoomBookVM", "date = $bookingDate, $dayOfWeek")

        val data = Firestore().getSelectedRoomBookings(roomId, bookingDate)
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            booking.doc_id = i.id

            if (booking.student_email != Firestore().getCurrentUserEmail()) {
                existingBookings.add(booking)
            }
        }

        if (dayOfWeek == 1 || dayOfWeek == 7) {
            errorMessage = "Cannot book discussion room on weekends."
            return false
        }
        else if (hour.toInt() < 9 || hour.toInt() > 18){
            errorMessage = "The time selected must between 9am and 6pm."
            return false
        }
        else if (minute != "30" && minute != "00") {
            errorMessage = "The time selected must 30 minutes interval."
            return false
        }
        else if (bookingDate?.compareTo(currentDate) == 0){
            if (hour.toInt() < currentHour.toInt()) {
                errorMessage = "The time selected must later than current time."
                return false
            }
            else if(hour.toInt() == currentHour.toInt() && minute.toInt() < currentMinute.toInt()) {
                errorMessage = "The time selected must later than current time."
                return false
            }
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

        return editMyBooking(docId, hashMap)
    }

    suspend fun editMyBooking(docId: String, hashMap: HashMap<String, Any>) : Boolean{
        val result = Firestore().editMyBooking(docId, hashMap)

        if(!result) {
            errorMessage = "Unable to book the room."
        }


        return result
    }

    suspend fun deleteMyBooking(docId: String) : Boolean{
        return Firestore().deleteMyBooking(docId)
    }

//    suspend fun getAllBookings() {
//        allBookingList.clear()
//
//        val data = Firestore().getAllBookings()
//        for (i in data?.documents!!){
//            val booking = i?.toObject(RoomBooking::class.java)!!
//            allBookingList.add(booking)
//        }
//    }

}