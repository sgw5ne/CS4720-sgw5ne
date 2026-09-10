package edu.virginia.cs4720.sgw5ne

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.ui.theme.Helloworldsgw5neTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Helloworldsgw5neTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    HelloScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HelloScreen(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var salutation by remember { mutableStateOf("Hello") }

    Column(modifier = modifier.padding(16.dp)) {
        Greeting(salutation = salutation, name = name)
        EnterName(name = name, onNameChange = { name = it })
        Salutations(selected = salutation, onSelectedChange = {salutation = it })
    }
}

@Composable
fun Greeting(name: String, salutation: String, modifier: Modifier = Modifier) {
    val displayName = if (name.isBlank()) "Your name here" else name
    Text(
        text = "$salutation, $displayName!",
        modifier = modifier
    )
}

@Composable
fun EnterName(name: String, onNameChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        singleLine = true
    )
}

@Composable
fun Salutations(selected: String, onSelectedChange: (String) -> Unit) {
    val options = listOf("Hello", "Hi There", "Bonjour")

    Column {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selected),
                    onClick = { onSelectedChange(option) }
                )
                Text(option)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelloWorldPreview() {
    Helloworldsgw5neTheme() {
        HelloScreen()
    }
}
