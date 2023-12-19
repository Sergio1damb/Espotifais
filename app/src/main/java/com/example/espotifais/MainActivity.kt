package com.example.espotifais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.espotifais.ui.theme.EspotifaisTheme


data class Cancion(val nombre: String, val cantantes: String, val caratula: Int, val duracion: String)

class CancionViewModel : ViewModel() {

    private val canciones = listOf(
        Cancion("Moonlight", "Cruz Cafuné, Alba Reche", R.drawable.moonlight, "03:45"),
        Cancion("CAMBIAR EL MUNDO", "Bejo, Cookin Soul", R.drawable.cambiarelmundo, "04:30"),
        Cancion("Guillao", "La Pantera, Juseph, Bdp Music", R.drawable.guillao, "02:50"),
        Cancion("Ganas", "Maikel Delacalle", R.drawable.ganas, "05:15"),
        Cancion("LEONARDO FLEXXO", "La$$ Suga', Cuki Music, MPadrums", R.drawable.leonardo, "03:10")
    )

    var indiceCancionActual = mutableStateOf(0)
    var cancionActual = mutableStateOf(canciones[indiceCancionActual.value])

    fun siguienteCancion() {
        indiceCancionActual.value = (indiceCancionActual.value + 1) % canciones.size
        cancionActual.value = canciones[indiceCancionActual.value]
    }

    fun cancionAnterior() {
        indiceCancionActual.value = (indiceCancionActual.value - 1 + canciones.size) % canciones.size
        cancionActual.value = canciones[indiceCancionActual.value]
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel = CancionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EspotifaisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayer(viewModel)
                }
            }
        }
    }
}

@Composable
fun MusicPlayer(viewModel: CancionViewModel) {
    val cancionActual by viewModel.cancionActual

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(cancionActual.nombre, style = MaterialTheme.typography.headlineLarge)
            Text(cancionActual.cantantes, style = MaterialTheme.typography.titleMedium)
        }
        Image(painterResource(id = cancionActual.caratula), contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.weight(4f).fillMaxSize().padding(16.dp).clip(RoundedCornerShape(8.dp)))
        Slider(value = 0.02f, onValueChange = {}, valueRange = 0f..1f, modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("00:00")
            Text(cancionActual.duracion)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = { }) { Icon(Icons.Default.Shuffle, contentDescription = null) }
            IconButton(onClick = { viewModel.cancionAnterior() }) { Icon(Icons.Default.SkipPrevious, contentDescription = null) }
            IconButton(onClick = { }, modifier = Modifier.background(shape = RoundedCornerShape(50.dp), color=Color.Blue)) { Icon(Icons.Default.PlayArrow, contentDescription=null)}
            IconButton(onClick = { viewModel.siguienteCancion() }) { Icon(Icons.Default.SkipNext, contentDescription=null)}
            IconButton(onClick = { }) { Icon(Icons.Default.Repeat, contentDescription=null)}
        }
    }
}
