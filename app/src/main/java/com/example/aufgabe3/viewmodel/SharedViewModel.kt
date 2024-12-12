package com.example.aufgabe3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aufgabe3.model.BookingEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/**
 * ViewModel to manage the list of booking entries and handle adding and deleting bookings.
 */
class SharedViewModel: ViewModel() {

    private val _bookingsEntries = MutableStateFlow<List<BookingEntry>>(emptyList())
    val bookingsEntries: StateFlow<List<BookingEntry>> = _bookingsEntries

    /**
     * Adds a new booking entry to the list.
     * @param arrivalDate The arrival date.
     * @param departureDate The departure date.
     * @param name The name of the person making the booking.
     */
    fun addBookingEntry(arrivalDate: LocalDate, departureDate: LocalDate, name: String) {
        val newEntry = BookingEntry(arrivalDate = arrivalDate, departureDate = departureDate, name = name)
        _bookingsEntries.value = _bookingsEntries.value + newEntry
    }

    /**
     * Deletes a booking entry from the list.
     * @param bookingEntry The booking entry to delete.
     */
    fun deleteBookingEntry(bookingEntry: BookingEntry) {
        _bookingsEntries.value = _bookingsEntries.value.filterNot { it == bookingEntry }
    }
}