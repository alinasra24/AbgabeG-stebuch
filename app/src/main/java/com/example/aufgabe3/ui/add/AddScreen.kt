package com.example.aufgabe3.ui.add

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.ui.theme.lightPurpleColor
import com.example.aufgabe3.ui.theme.mediumPurpleColor
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * Composable function for adding a new booking entry with a name and date range.
 * Displays a form to input the name and select a date range.
 *
 * @param navController Navigation controller for screen navigation.
 * @param sharedViewModel The view model managing booking data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }

    var showDateRangePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Booking Entry") }, // Titel der AppBar
                navigationIcon = {
                    // Zurück-Button in der AppBar
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)

        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text("Select Date Range") },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker = true },

                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(24.dp)) // Abstand zwischen Elementen

            val context= LocalContext.current

            Button(
                onClick = {

                    if(name.isBlank()&&( arrivalDate == null || departureDate == null)){
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                    else if(arrivalDate == null || departureDate == null){
                        Toast.makeText(context, "Please select a date range", Toast.LENGTH_SHORT).show()
                    }
                    else if(name.isBlank()) {
                        Toast.makeText(context, "Name cant be empty", Toast.LENGTH_SHORT).show()
                    }

                    else {

                        sharedViewModel.addBookingEntry(arrivalDate!!, departureDate!!, name)
                        name= ""
                        arrivalDate=null
                        departureDate=null
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightPurpleColor

                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", color= mediumPurpleColor)
            }
        }

    }

    if (showDateRangePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { dateRange ->
                dateRange.first?.let { startMillis ->
                    dateRange.second?.let { endMillis ->
                        arrivalDate = Instant.ofEpochMilli(startMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        departureDate = Instant.ofEpochMilli(endMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                }
                showDateRangePicker = false
            },
            onDismiss = { showDateRangePicker = false }
        )
    }
}

/**
 * Modal for selecting a date range.
 * Displays a date picker dialog with start and end date selection.
 *
 * @param onDateRangeSelected Callback to return the selected date range (start and end).
 * @param onDismiss Callback to dismiss the date picker modal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(

    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit

) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val todayMillis = System.currentTimeMillis()-86400000
                    val selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis
                    val selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis

                    if (selectedStartDateMillis != null && selectedStartDateMillis < todayMillis) {
                        return@TextButton
                    }
                    if (selectedEndDateMillis != null && selectedEndDateMillis < todayMillis) {
                        return@TextButton
                    }

                    onDateRangeSelected(Pair(selectedStartDateMillis, selectedEndDateMillis))
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
                .background(lightPurpleColor)
                .border(2.dp, Color.Black)
        ){
            DateRangePicker(
                state = dateRangePickerState,
                title = { Text("  Select date range", color = mediumPurpleColor)},
                showModeToggle = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}