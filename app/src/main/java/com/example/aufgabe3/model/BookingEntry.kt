package com.example.aufgabe3.model

import java.time.LocalDate

// TODO Customise the data structure for a book entry according to the requirements

/**
 * Represents a booking entry for a guest.
 *
 * @param arrivalDate The date the guest arrives.
 * @param departureDate The date the guest departs.
 * @param name The name associated with the booking.
 */
data class BookingEntry(
    val arrivalDate: LocalDate, // Das Ankunftsdatum des Gastes
    val departureDate: LocalDate, // Das Abreisedatum des Gastes
    val name: String
)
