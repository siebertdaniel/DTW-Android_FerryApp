package cloud.siebert.ferryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cloud.siebert.ferryapp.ui.theme.FerryAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FerryAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    val checkedState = remember { mutableStateOf(true) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                            Text(
                                text = "Notification aktivieren",
                                fontSize = 16.sp,

                            )
                            Switch(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it },
                                Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {

                                    if (checkedState.value) {
                                        //service.showNotification("*Furzgeräusch*")
                                        onClick()
                                    }
                                },
                                Modifier.padding(horizontal = 8.dp)
                            )
                            {
                                Text(text = "Berechtigung")
                            }
                            Button(
                                onClick = {

                                    if (checkedState.value) {
                                        //service.showNotification("*Furzgeräusch*")
                                        onClick()
                                    }
                                },
                                Modifier.padding(horizontal = 8.dp)
                            )
                            {
                                Text(text = "Notification testen")
                            }
                        }
                        NavigatorFerry()
                        YourNextTrip()
                    }
                }
            }
        }
    }

    private fun onClick() {
        val service = FerryNotificationService(applicationContext)
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            service.showNotification()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleTextField() {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            }
        )
    }


    @Preview
    @Composable
    fun YourNextTrip() {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "Your Next Trip",
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    fontSize = 26.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(painterResource(id = R.drawable.icon_73), contentDescription = "Icon Ferry")
            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Monday, 10.07.2023", Modifier.padding(horizontal = 16.dp))
                Text(text = "09:10")
            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Status: ", Modifier.padding(horizontal = 16.dp))
                Text(text = "On Time", color = Color.Green)
            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "\uD83D\uDCCC Ernst-August-Schleuse um 09:10", Modifier.padding(horizontal = 16.dp))
            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "⛴️ 20 Minuten", Modifier.padding(horizontal = 16.dp))
            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "\uD83C\uDFC1 Landungsbrücken um 09:30", Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    @Preview
    @Composable
    fun NavigatorFerry() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SimpleTextField()
            SimpleTextField()
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Abfahrt")
                Button(
                    onClick = {
                        //TODO: Something
                    },
                    Modifier.padding(horizontal = 16.dp)
                )
                {
                    Text(text = "10. Jul 2023")
                }
                Button(
                    onClick = {
                        //TODO: Something
                    },
                    Modifier.padding(horizontal = 16.dp)
                )
                {
                    Text(text = "09:10")
                }
            }
            Column() {
                Button(
                    onClick = {
                        //TODO: Something
                    },
                    Modifier.padding(horizontal = 16.dp)
                )
                {
                    Text(text = "Suche Verbindung")
                }
            }
        }
    }
}
