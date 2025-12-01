package com.example.helpja

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

class ShareLocationActivity : ComponentActivity() {

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getLocation()
            }
        }

    private lateinit var locationText: MutableState<String>
    private lateinit var shareLink: MutableState<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationText = mutableStateOf("Localização ainda não obtida")
        shareLink = mutableStateOf(null)

        setContent {
            ShareLocationScreen(
                locationText.value,
                shareLink.value,
                onGetLocationClick = { requestLocationPermission() },
                onShareClick = { shareLocation() },
                onBackClick = { finish() }
            )
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getLocation()
        }
    }

    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                shareLink.value = "https://maps.google.com/?q=$latitude,$longitude"
                locationText.value = "Lat: $latitude, Long: $longitude"
            } else {
                locationText.value = "Não foi possível obter a localização."
            }
        }
    }

    private fun shareLocation() {
        shareLink.value?.let { link ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Preciso de ajuda! Minha localização é: $link")
            }
            startActivity(Intent.createChooser(intent, "Compartilhar via"))
        }
    }
}

@Composable
fun ShareLocationScreen(
    locationText: String,
    shareLink: String?,
    onGetLocationClick: () -> Unit,
    onShareClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compartilhar Localização", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))

        Button(onClick = onGetLocationClick) {
            Text("📍 Obter Localização")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(locationText)

        Spacer(modifier = Modifier.height(24.dp))

        shareLink?.let {
            Button(onClick = onShareClick) {
                Text("📤 Compartilhar Localização")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onBackClick) {
            Text("⬅ Voltar")
        }
    }
}
