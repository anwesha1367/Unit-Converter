package com.example.unitconverter

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UConUI() {
    var inputValue by remember { mutableStateOf(" ") }
    var outputValue by remember { mutableStateOf(" ") }

    var inputUnit by remember { mutableStateOf("Meter") }
    var outputUnit by remember { mutableStateOf("Meter") }
    var isinputExpanded by remember { mutableStateOf(false) }
    var isoutputExpanded by remember { mutableStateOf(false) }
    var inputConversionFactor by remember { mutableStateOf(1.0) }
    var outputConversionFactor by remember { mutableStateOf(1.0) }

    fun convertUnit() {
        val input = inputValue.toDoubleOrNull() ?: 0.0
        val result = ((input * inputConversionFactor / outputConversionFactor) * 100).roundToInt() / 100.0
        outputValue = result.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter= painterResource(id=R.drawable.uc_logo), contentDescription = "")
        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                convertUnit()
            },
            label = { Text(text = "Enter Value") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropDown(
                label = inputUnit,
                expanded = isinputExpanded,
                onExpandedChange = { isinputExpanded = it },
                onOptionSelected = { unit, factor ->
                    inputUnit = unit
                    inputConversionFactor = factor
                    convertUnit()
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            DropDown(
                label = outputUnit,
                expanded = isoutputExpanded,
                onExpandedChange = { isoutputExpanded = it },
                onOptionSelected = { unit, factor ->
                    outputUnit = unit
                    outputConversionFactor = factor
                    convertUnit()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Result: $outputValue $outputUnit",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DropDown(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String, Double) -> Unit
) {
    Box {
        Button(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = label)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            listOf(
                "Centimeter" to 0.01,
                "Meter" to 1.0,
                "Feet" to 0.3048,
                "Millimeter" to 0.001
            ).forEach { (unit, factor) ->
                DropdownMenuItem(
                    text = { Text(text = unit) },
                    onClick = {
                        onExpandedChange(false)
                        onOptionSelected(unit, factor)
                    }
                )
            }
        }
    }
}
