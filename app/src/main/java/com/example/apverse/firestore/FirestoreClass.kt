package com.example.apverse.firestore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.apverse.MainActivity
import com.example.apverse.model.*
import com.example.apverse.ui.librarian.book.LBookDetailsFragment
import com.example.apverse.ui.librarian.book.LBookFragment
import com.example.apverse.ui.librarian.book.LBookReservationFragment
import com.example.apverse.ui.librarian.home.LHomeFragment
import com.example.apverse.ui.librarian.home.LHomeViewModel
import com.example.apverse.ui.librarian.room.LRoomFragment
import com.example.apverse.ui.login.LoginFragment
import com.example.apverse.ui.student.book.SBookDetailsFragment
import com.example.apverse.ui.student.book.SBookFragment
import com.example.apverse.ui.student.computer.SComputerFragment
import com.example.apverse.ui.student.room.SRoomBookFragment
import com.example.apverse.ui.student.room.SRoomFragment
import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserEmail():String{
        val User= FirebaseAuth.getInstance().currentUser

        var email = " "
        if (User != null){
            email = User.email.toString()
        }
        return email
    }

    fun getUserDetails(fragment: Fragment){
        var user: Users = Users()

        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.USER_EMAIL, getCurrentUserEmail())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    user = document.toObject(Users::class.java)!!
                }

                when (fragment) {
                    is SBookDetailsFragment ->
                        fragment.reserveBook(user)
                }

                if(documents == null){
                    Log.e("ApVerse::Firestore", "No User Found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ApVerse::Firebase", "Error", exception)
            }
    }

    fun getUserDetails(activity: MainActivity){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.USER_EMAIL, getCurrentUserEmail())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.i("ApVerse", "${document.id} => ${document.data}")
                    val user = document.toObject(Users::class.java)!!
                    Log.i("ApVerse", user.user_email)
                    Log.i("ApVerse", user.user_name)
                    Log.i("ApVerse", user.user_type)
                    activity.DisplayHeaderInfo(user)
                }

                if(documents == null){
                    Log.e("ApVerse::Firestore", "No User Found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ApVerse::Firebase", "Error", exception)
            }
    }

    fun getDocumentId(
        fragment: Fragment,
        table_name: String,
        inputHashMap: HashMap<String, Any>,
        checkingHashMap: HashMap<String, Any>
    ){
        var documentId: String = ""
        var field_name = checkingHashMap.keys.first()
        var field_data = checkingHashMap.getValue(field_name)

        mFireStore.collection(table_name)
            .whereEqualTo(field_name, field_data)
            .addSnapshotListener{ snapshot, e ->
                if(e != null){
                    Log.e("ApVerse::Firebase", "Snapshot listen failed", e)
                }

                if(snapshot != null){
                    for (i in snapshot.documents){
                        documentId = i.id
                        Log.i("ApVerse::Firebase", "documentId: "+documentId)

                        when(fragment){
                            is SComputerFragment -> {
//                                fragment.reload()
                                val key =  inputHashMap.keys.first()
                                val value = inputHashMap.getValue(key)
                                val key2 = inputHashMap.keys.elementAt(1)
                                val value2 = inputHashMap.getValue(key2)
                                Log.i("ApVerse:Firebase", key+", "+value)
                                Log.i("ApVerse:Firebase", key2+", "+value2)

                                updateComputerStatus(fragment, inputHashMap, documentId)
                            }
                        }
                    }
                }
            }

    }

//    fun getDocumentId(
//        table_name: String,
//        field_name: String,
//        field_data: Any
//    ): String {
//        var documentId: String = ""
//
//        mFireStore.collection(table_name)
//            .whereEqualTo(field_name, field_data)
//            .get()
//            .addOnSuccessListener { document ->
//                documentId = document.toString()
//                Log.i("ApVerse::Firebase", "documentId: "+documentId)
//
//            }
//            .addOnFailureListener { e ->
//                Log.e("ApVerse::Firebase", "Get Document Id Failed.", e)
//            }
//
//        return documentId
//    }

    fun getRooms(fragment: Fragment) {
        val roomList: ArrayList<Rooms> = ArrayList()

        mFireStore.collection(Constants.ROOMS)
            .orderBy(Constants.ROOM_ID)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents){
                    var room = i.toObject(Rooms::class.java)!!
                    roomList.add(room)
                    Log.i("ApVerse", room.room_id+", "+room.capacity)
                }

                when(fragment){
                    is SRoomFragment -> {
                        fragment.successLoadRooms(roomList)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ApVerse::Firebase", "Get Rooms Failed.", e)
            }
    }

    fun validateRoomBooking(fragment: SRoomBookFragment, roomBookingInfo:RoomBooking){
        val delim = ":"
        val hour = roomBookingInfo.time.substringBefore(delim)

        if(hour.toInt() < 9 || hour.toInt() > 18){
            fragment.saveRoomBookingFailure("Time should be between 9am to 6pm.")
        }else{
            createRoomBooking(fragment, roomBookingInfo)
        }
    }

    fun createRoomBooking(fragment: SRoomBookFragment, roomBookingInfo:RoomBooking){
        mFireStore.collection(Constants.ROOM_BOOKING)
            .document()
            .set(roomBookingInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.saveRoomBookingSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("ApVerse::Firebase", "Add Room Booking Failed.", e)
            }
    }

    fun getRoomBookings(fragment: Fragment){
        val bookingList: ArrayList<RoomBooking> = ArrayList()
        var count = 0

        mFireStore.collection(Constants.ROOM_BOOKING)
            .orderBy(Constants.DATE)
            .orderBy(Constants.TIME)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents){
                    val booking = i.toObject(RoomBooking::class.java)

                    val current = Calendar.getInstance().time
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val dataDate = booking?.date
                    val today = dateFormatter.format(current)
                    val student = booking?.student_name

                    Log.i("ApVerse::Firebase", "student: $student")
                    Log.i("ApVerse::Firebase", "dataDate: $dataDate")

                    if(dataDate?.compareTo(today) == 0){
                        bookingList.add(booking)
                        count += 1
                        Log.i("ApVerse::Firebase", "count: $count")
                    }
                    else if(dataDate?.compareTo(today)!! > 0){
                        bookingList.add(booking)
                        count += 1
                        Log.i("ApVerse::Firebase", "count: $count")
                    }
                }

                when (fragment) {
                    is LRoomFragment -> {
                        Log.i("ApVerse::Firebase", "successLoadRoomBoookings()")
                        fragment.successLoadRoomBoookings(bookingList)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ApVerse::Firebase", "Get Bookings Failed.", e)

                when (fragment) {
                    is LRoomFragment -> {
                        fragment.failedLoadRoomBoookings("Unable to retrieve room booking info.")
                    }
                }

            }

//            .addSnapshotListener { snapshot, e ->
//
//                if(e != null){
//                    Log.e("ApVerse::Firestore", "Listen Failed", e)
//                    return@addSnapshotListener
//                }
//
//                if(snapshot != null) {
//                    val documents = snapshot.documents
//
//                    documents.forEach{
//                        val booking = it.toObject(RoomBooking::class.java)
//
//                        val current = Calendar.getInstance().time
//                        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
//                        val dataDate = booking?.date
//                        val today = dateFormatter.format(current)
//                        val student = booking?.student_name
//
//                        Log.i("ApVerse::Firebase", "student: $student")
//                        Log.i("ApVerse::Firebase", "dataDate: $dataDate")
//
//                        if(booking != null) {
//                            if(dataDate?.compareTo(today) == 0){
//                                bookingList.add(booking)
//                                count += 1
//                                Log.i("ApVerse::Firebase", "count: $count")
//                            }
//                            else if(dataDate?.compareTo(today)!! > 0){
//                                bookingList.add(booking)
//                                count += 1
//                                Log.i("ApVerse::Firebase", "count: $count")
//                            }
//                        }
//                    }
//
//                    Log.i("ApVerse::Firebase", "count: $count")
//
//                    when (fragment) {
//                        is LRoomFragment -> {
//                            Log.i("ApVerse::Firebase", "successLoadRoomBoookings()")
//                            fragment.successLoadRoomBoookings(bookingList)
//                        }
//                    }
//                }
//            }
    }

    fun getBookingCount(fragment: Fragment, date: String) {
        val bookingList: ArrayList<RoomBooking> = ArrayList()

        mFireStore.collection(Constants.ROOM_BOOKING)
            .whereEqualTo(Constants.DATE, date)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents){
                    val booking = i.toObject(RoomBooking::class.java)!!
                    bookingList.add(booking)
                }

                when (fragment) {
                    is LHomeFragment -> {
                        Log.i("ApVerse::Firebase", "success getBookingCount()")
                        fragment.successGetBookingCount(bookingList)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ApVerse::Firebase", "Get Bookings Failed.", e)
            }
    }

    fun getComputers(fragment: Fragment) {
        val computerList: ArrayList<Computers> = ArrayList()
        var isUsing = false

        mFireStore.collection(Constants.COMPUTERS)
            .orderBy(Constants.COMPUTER_ID)
            .addSnapshotListener{ snapshot, e ->

                if(e != null){
                    Log.e("ApVerse::Firestore", "Listen Failed", e)
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    val documents = snapshot.documents

                    documents.forEach{
                        val computer = it.toObject(Computers::class.java)

                        if (computer != null){
                            computer.doc_id = it.id
                            computerList.add(computer)

                            if(computer.student_email == getCurrentUserEmail()){
                                isUsing = true
                            }
                        }
                    }

                    when(fragment){
                        is SComputerFragment -> {
                            Log.i("ApVerse::Firebase", "successLoadComputers()")
                            fragment.successLoadComputers(computerList, isUsing)
                        }
                    }
                }
            }
//            .get()
//            .addOnSuccessListener { document ->
//                for (i in document.documents){
//                    var computer = i.toObject(Computers::class.java)!!
//                    computerList.add(computer)
//
//                    if(computer.student_email == getCurrentUserEmail()){
//                        isUsing = true
//                    }
//
//                    Log.i("ApVerse::Firebase", document?.metadata.toString())
//                }
//
//                Log.i("ApVerse::Firebase", "isUsing: "+isUsing)
//                when(fragment){
//                    is SComputerFragment -> {
//                        Log.i("ApVerse::Firebase", "successLoadComputers()")
//                        fragment.successLoadComputers(computerList, isUsing)
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("ApVerse::Firebase", "Get Computers Failed.", e)
//            }
    }

    fun updateComputerStatus(
        fragment: Fragment,
        computerHashMap: HashMap<String,Any>,
        documentId: String
    ) {
        Log.i("ApVerse::Firebase","Enter updateComputerStatus()")

        mFireStore.collection(Constants.COMPUTERS)
            .document(documentId)
            .update(computerHashMap)
            .addOnSuccessListener { document->
                Log.i("ApVerse::Firebase","Success Update Computer Status")
                when(fragment){
                    is SComputerFragment ->{
                        fragment.successUpdateComputerStatus()
                    }
                }
            }
            .addOnFailureListener { e->
                Log.e("ApVerse::Firebase","Error Update Computer Status",e)
                when(fragment){
                    is SComputerFragment ->{
                        fragment.failedUpdateComputerStatus()
                    }
                }
            }
    }

    fun getBooks(fragment: Fragment){
        val bookList: ArrayList<Books> = ArrayList()

        mFireStore.collection(Constants.BOOKS)
            .orderBy(Constants.BOOK_TITLE)
            .addSnapshotListener{ snapshot, e ->

                if(e != null){
                    Log.e("ApVerse::Firestore", "Listen Failed", e)
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    val documents = snapshot.documents

                    documents.forEach{
                        val book = it.toObject(Books::class.java)

                        if (book != null){
                            book.doc_id = it.id
                            bookList.add(book)
                        }
                    }

                    when(fragment){
                        is SBookFragment -> {
                            Log.i("ApVerse::Firebase", "SBookFragment - successLoadBooks()")
                            fragment.successLoadBooks(bookList)
                        }

                        is LBookFragment -> {
                            Log.i("ApVerse::Firebase", "SBookFragment - successLoadBooks()")
                            fragment.successLoadBooks(bookList)
                        }
                    }
                }
            }
    }

//    fun getBookDetails(bookId: String){
//        mFireStore.collection(Constants.BOOKS)
//            .whereEqualTo(Constants.BOOK_ID, bookId)
//            .get()
//            .addOnSuccessListener { documents ->
//                for(document in documents) {
//                    var book = document.toObject(Books::class.java)
//                    Constants.L_BOOK_TITLE = book.book_title
//                    Constants.L_BOOK_IMG = book.book_image
//                    book.doc_id = document.id
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("ApVerse::Firebase", "Error getting book details", e)
//            }
//    }

    fun getBookDetails(fragment: Fragment, bookId: String){
        mFireStore.collection(Constants.BOOKS)
            .whereEqualTo(Constants.BOOK_ID, bookId)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    var book = document.toObject(Books::class.java)
                    book.doc_id = document.id

                    when (fragment) {
                        is SBookDetailsFragment -> {
                            fragment.showBookDetails(book)
                        }

                        is LBookDetailsFragment -> {
                            fragment.showBookDetails(book)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ApVerse::Firebase", "Error getting book details", e)

                when (fragment) {
                    is SBookDetailsFragment -> {
                        fragment.failedGetBookDetails()
                    }

                    is LBookDetailsFragment -> {
                        fragment.failedGetBookDetails()
                    }
                }
            }
    }

    fun updateBookStatus(
        fragment: Fragment,
        docId: String,
        bookHashMap: HashMap<String, Any>) {
            mFireStore.collection(Constants.BOOKS)
                .document(docId)
                .update(bookHashMap)
                .addOnSuccessListener { document->
                    Log.i("ApVerse::Firebase","Success Update Book Status")

                    when(fragment){
                        is LBookDetailsFragment ->{
                            fragment.successUpdateBookStatus()
                        }
                    }
                }
                .addOnFailureListener { e->
                    Log.e("ApVerse::Firebase","Error Update Book Status",e)

                    when(fragment){
                        is LBookDetailsFragment ->{
                            fragment.failedUpdateBookStatus()
                        }
                    }
                }
    }

    fun getReservations(fragment: Fragment) {
        val reservationList: ArrayList<BookReservation> = ArrayList()

        mFireStore.collection(Constants.BOOK_RESERVATION)
            .orderBy(Constants.DATE)
            .orderBy(Constants.TIME)
            .addSnapshotListener{ snapshot, e ->

                if(e != null){
                    Log.e("ApVerse::Firestore", "Listen Failed", e)
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    val documents = snapshot.documents

                    documents.forEach{
                        val reservation = it.toObject(BookReservation::class.java)

                        if (reservation != null){
                            reservation.doc_id = it.id
                            reservationList.add(reservation)
                            Log.i("ApVerse::Firestore", reservation.student_name+", "+reservation.reservation_status+", "+reservation.ready)
                        }
                    }

                    when(fragment){
                        is LBookReservationFragment -> {
                            Log.i("ApVerse::Firebase", "successLoadReservations()")
                            fragment.successLoadReservation(reservationList)
                        }

                        is LHomeFragment -> {
                            Log.i("ApVerse::Firebase", "successLoadReservations()")
                            fragment.successGetReservationCount(reservationList)
                        }
                    }
                }
            }
    }

    fun getBookReservation(fragment: Fragment, bookId: String) {
        val reservationList: ArrayList<BookReservation> = ArrayList()
        var hasReservation: Int = 0

        mFireStore.collection(Constants.BOOK_RESERVATION)
            .whereEqualTo(Constants.BOOK_ID, bookId)
            .orderBy(Constants.DATE)
            .addSnapshotListener{ snapshot, e ->

                if(e != null){
                    Log.e("ApVerse::Firestore", "Listen Failed", e)
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    val documents = snapshot.documents

                    documents.forEach{
                        val reservation = it.toObject(BookReservation::class.java)

                        if (reservation != null){
                            reservation.doc_id = it.id
                            reservationList.add(reservation)
                            Log.i("ApVerse::Firestore", reservation.student_name+", "+reservation.reservation_status+", "+reservation.ready)

                            if(reservation.student_email == getCurrentUserEmail()){
                                hasReservation += 1
                            }
                        }
                    }

                    when(fragment){
                        is SBookDetailsFragment -> {
                            Log.i("ApVerse::Firebase", "SBookDetailsFragment - successLoadReservations()")
                            fragment.successLoadReservation(reservationList, hasReservation)
                        }

                        is LBookDetailsFragment -> {
                            Log.i("ApVerse::Firebase", "LBookDetailsFragment - successLoadReservations()")
                            fragment.successLoadReservation(reservationList)
                        }
                    }
                }
            }
    }

    fun reserveBook(fragment: Fragment, reservationInfo: NewBookReservation) {
        mFireStore.collection(Constants.BOOK_RESERVATION)
            .document()
            .set(reservationInfo, SetOptions.merge())
            .addOnSuccessListener {
                when (fragment) {
                    is SBookDetailsFragment -> {
                        fragment.successReserveBook()
                    }
                }
            }
    }

    fun validateReservation(
        fragment: Fragment,
        docId: String,
        reservationHashMap: HashMap<String, Any>,
        bookId: String
    ) {
        var bookStatus: String  = ""

        mFireStore.collection(Constants.BOOKS)
            .whereEqualTo(Constants.BOOK_ID, bookId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val book = document.toObject(Books::class.java)!!
                    book.doc_id = document.id
                    bookStatus = book.book_status
                }

                if(bookStatus == "Borrowed") {
                    when (fragment) {
                        is LBookReservationFragment -> {
                            fragment.failedUpdateReservation("Cannot send alert because the book is being borrowed.")
                        }
                    }
                }
                else{
                    updateReservation(fragment, docId, reservationHashMap)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ApVerse::Firebase", "Error", exception)
            }
    }

    fun updateReservation(
        fragment: Fragment,
        docId: String,
        reservationHashMap: HashMap<String, Any>
    ) {

        mFireStore.collection(Constants.BOOK_RESERVATION)
            .document(docId)
            .update(reservationHashMap)
            .addOnSuccessListener { document->
                Log.i("ApVerse::Firebase","Success Update Reservation Status")

                when(fragment){
                    is LBookReservationFragment ->{
                        fragment.successUpdateReservation()
                    }
                }
            }
            .addOnFailureListener { e->
                Log.e("ApVerse::Firebase","Error Update Reservation Status",e)

                when(fragment){
                    is LBookReservationFragment ->{
                        fragment.failedUpdateReservation("Reservation update failed.")
                    }
                }
            }
    }

    fun deleteReservation(fragment: Fragment, docId: String){
        mFireStore.collection(Constants.BOOK_RESERVATION)
            .document(docId)
            .delete()
            .addOnSuccessListener {
                when(fragment){
                    is LBookReservationFragment ->{
                        //fragment.hideProgressDialog()
                        fragment.successDeleteReservation()
                    }
                }
            }
            .addOnFailureListener { e->
                Log.e(fragment.javaClass.simpleName,"Error deleting subplan",e)

                when(fragment){
                    is LBookReservationFragment ->{
                        fragment.failedDeleteReservation()
                    }
                }
            }
    }

//    fun getUserDetails(activity: MainActivity): Users {
////    fun getUserDetails(activity: MainActivity) {
//        var user: Users = Users()
//
//        // Here we pass the collection name from which we wants the data.
//        mFireStore.collection(Constants.USERS)
//            // The document id to get the Fields of user.
//            .document(getCurrentUserEmail())
//            .get()
//            .addOnSuccessListener { document ->
//                // Here we have received the document snapshot which is converted into the User Data model object.
//                user = document.toObject(Users::class.java)!!
//                Log.i("ApVerse", "User found.")
//
////                  val user = document.toObject(Users::class.java)!!
////                activity.DisplayHeaderInfo(user)
//            }
//            .addOnFailureListener { e ->
//                Log.e(
//                    "ApVerse",
//                    "Error while getting user details.",
//                    e
//                )
//            }
//
//        return user
//    }

//    fun getUserDetails(fragment: Fragment) {
//
//        // Here we pass the collection name from which we wants the data.
//        mFireStore.collection(Constants.USERS)
//            // The document id to get the Fields of user.
//            .document(getCurrentUserEmail())
//            .get()
//            .addOnSuccessListener { document ->
//
//                Log.i(fragment.javaClass.simpleName, document.toString())
//
//                // Here we have received the document snapshot which is converted into the User Data model object.
//                val user = document.toObject(User::class.java)!!
//
////                val sharedPreferences =
////                    fragment.requireActivity().getSharedPreferences(
////                        Constants.MYPLANNER_PREFERENCES,
////                        Context.MODE_PRIVATE
////                    )
////
////                // Create an instance of the editor which is help us to edit the SharedPreference.
////                val editor: SharedPreferences.Editor = sharedPreferences.edit()
////                editor.putString(
////                    Constants.LOGGED_IN_USERNAME,
////                    "${user.username}"
////                )
////                editor.apply()
////
////                when (fragment) {
////                    is LoginFragment -> {
////                        // Call a function of base activity for transferring the result to it.
////                        fragment.userLoggedInSuccess(user)
////                    }
////
////                    is ProfileFragment -> {
////                        // Call a function of base activity for transferring the result to it.
////                        fragment.DisplayUserDetailsSuccess(user)
////                    }
////                }
////                fragment.dis
//            }
//            .addOnFailureListener { e ->
//                // Hide the progress dialog if there is any error. And print the error in log.
////                when (fragment) {
////                    is LoginFragment -> {
////                        fragment.hideProgressDialog()
////                    }
////                    is ProfileFragment -> {
////                        fragment.hideProgressDialog()
////                    }
////                }
//
//                Log.e(
//                    fragment.javaClass.simpleName,
//                    "Error while getting user details.",
//                    e
//                )
//            }
//    }
}