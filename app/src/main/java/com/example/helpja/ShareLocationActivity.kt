package com.example.helpja

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

class ShareLocationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShareLocationScreen()
        }
    }
}

@Composable
fun ShareLocationScreen() {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Localização ainda não obtida") }
    var shareLink by remember { mutableStateOf<String?>(null) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compartilhar Localização", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))

        Button(onClick = {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as ComponentActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        shareLink = "https://maps.google.com/?q=$latitude,$longitude"
                        locationText = "Lat: $latitude, Long: $longitude"
                    } else {
                        locationText = "Não foi possível obter a localização."
                    }
                }
            }
        }) {
            Text("📍 Obter Localização")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(locationText)

        Spacer(modifier = Modifier.height(24.dp))

        shareLink?.let { link ->
            Button(onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Preciso de ajuda! Minha localização é: $link")
                }
                context.startActivity(Intent.createChooser(intent, "Compartilhar via"))
            }) {
                Text("📤 Compartilhar Localização")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { (context as ComponentActivity).finish() }) {
            Text("⬅ Voltar")
        }
    }
}
