package com.example.helpja

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.jvm.java
import android.net.Uri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           /* HelpJaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
            HelpJa()
        }
    }
}

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelpJaTheme {
        Greeting("Android")
    }
}*/

@Composable
fun HelpJa(){
    val context = LocalContext.current
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "HelpJa",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                EmergencyButton("🚑 SAMU (192)") {
                    dialNumber(context,"192")
                }
                EmergencyButton("🚒 Bombeiros (193)") {
                    dialNumber(context,"193")
                }
                EmergencyButton("🚓 Polícia (190)") {
                    dialNumber(context,"190")
                }
                EmergencyButton("🏥 Hospital Próximo") {
                    openMaps(context,"hospital")
                }

                EmergencyButton("📍 Compartilhar Localização") {
                    val intent = Intent(context, ShareLocationActivity::class.java)
                    context.startActivity(intent)
                }

            }
        }
    }
}

@Composable
fun EmergencyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(60.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}

fun dialNumber(context: Context, number: String)  {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    context.startActivity(intent)
}

fun openMaps(context: Context, query: String){
    val gmmIntentUri = Uri.parse("geo:0,0?q=$query")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}
