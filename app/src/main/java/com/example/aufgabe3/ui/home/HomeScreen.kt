package com.example.aufgabe3.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Displays the list of booking entries with an option to add new ones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val bookingsEntries by sharedViewModel.bookingsEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Entries") } // Titel der oberen AppBar
            )
        },
        floatingActionButton = {

            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add booking")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {


            // TODO inform the user if no bookingsEntries otherwise LazyColumn for bookingsEntries
            if (bookingsEntries.isEmpty()) {
                Text(
                    text = "No bookings entries available.",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {

                LazyColumn (
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(bookingsEntries) { booking ->

                        BookingEntryItem(
                            booking = booking,
                            onDeleteClick = {

                                sharedViewModel.deleteBookingEntry(booking)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays a single booking entry with the option to delete it.
 * Composable for displaying individual booking entries in the list.
 */
@Composable
fun BookingEntryItem(
    booking: BookingEntry,
    onDeleteClick: () -> Unit
) {

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = booking.name, // TODO display booking name
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    // TODO display date in right format
                    text =  "${LocalDate.parse(booking.arrivalDate.toString()).format(dateFormatter)}-"+LocalDate.parse(booking.departureDate.toString()).format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete booking")
            }
        }
    }
}
