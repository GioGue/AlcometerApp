package com.example.alcometerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.alcometerapp.ui.theme.AlcometerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcometerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AlcolmeterScreen()
                }
            }
        }
    }
}

@Composable
fun AlcolmeterScreen() {

    var weightInput by remember { mutableStateOf("") }
    val weight : Int = weightInput.toIntOrNull() ?:0
    var male by remember { mutableStateOf(true) }
    //var nBottles by remember { mutableStateOf(1.3f) }
    var nBottleInput by remember { mutableStateOf("") }
    val nBottle : Int = nBottleInput.toIntOrNull() ?:0

    var nHours by remember{ mutableStateOf(1) }

    var result by remember { mutableStateOf(0.0f) }





    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement= Arrangement.spacedBy(8.dp),
    ){
        Heading(title = "Alcolmeter")
        WeightField(weightInput = weightInput, onValueChange = {weightInput = it})
        GenderChoices(male = male, setGenderMale = {male = it})
        //NumberBottleList(onClick = {nBottles = it})
        NumberBottle(nBottleInput = nBottleInput, onValueChange = {nBottleInput = it})
        SelectHours(onClick = {nHours = it})
        Text(text = String.format("%.2f", result).replace(',', '.'),
        color = MaterialTheme.colors.secondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
            )
        Calculation(male = male, weight= weight, nBottles = nBottle, nHours = nHours, setResult = {result = it})
    }


}
@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold, //I've added this
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)

    )
}

@Composable
fun WeightField(weightInput: String, onValueChange: (String) -> Unit){
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = {Text(text= "Enter weight")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit){
    Column(
        modifier = Modifier.selectableGroup(),
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = male,
                onClick = { setGenderMale(true)}
            )
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = !male, //female
                onClick = { setGenderMale(false)},

            )
            Text(text = "Female")
        }
    }
}


@Composable
fun NumberBottle(nBottleInput: String, onValueChange: (String) -> Unit){
    OutlinedTextField(
        value = nBottleInput,
        onValueChange = onValueChange,
        label = {Text(text= "Enter number of bottles")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun SelectHours(onClick: (Int)-> Unit){
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("1") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val items = listOf("1", "2", "3", "4", "5", "6", "7", "8","9","10")

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Column(
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
            label = {Text(text = "Select hours")},
            trailingIcon = {Icon(icon, "content description",
                Modifier.clickable { expanded = !expanded })},
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = { selectedText = label
                    var nHours: Int = when (label) {
                        "1" -> 1
                        "2" -> 2
                        "3" -> 3
                        "4" -> 4
                        "5" -> 5
                        "6" -> 6
                        "7"-> 7
                        "8"-> 8
                        "9" -> 9
                        "10"-> 10
                        else -> 1
                    }
                    onClick(nHours)
                    expanded = false

                }) {
                    Text(text = label)

                }

            }

        }
    }
}

@Composable
fun Calculation(male: Boolean, weight: Int, nBottles: Int, nHours: Int,setResult:(Float) -> Unit ){
    var litres = nBottles * 0.33;
    var grams = litres * 8 * 4.5;
    var burning = (weight / 10.0);
    var gramsLeft = (grams - (burning * nHours));
    var result = 0.0


    Button(
        onClick = {
            if (male){
               result = gramsLeft/(weight * 0.7)
                if (result > 0)
                    setResult(result.toFloat())
                else setResult(0f)
            }else {
                result = gramsLeft/(weight * 0.6)
                if (result > 0)
                    setResult(result.toFloat())
                else setResult(0f)

            }
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "Calculate")
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AlcometerAppTheme {
        AlcolmeterScreen()
    }
}