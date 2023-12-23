package com.example.espotifais

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
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
import androidx.media3.common.MediaItem
import com.example.espotifais.ui.theme.EspotifaisTheme
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlin.random.Random

data class Cancion(val nombre: String, val cantantes: String, val caratula: Int, val duracion: String, val ruta: String)

class CancionViewModel(private val context: Context) : ViewModel() {

    private val canciones = listOf(
        Cancion("Moonlight", "Cruz Cafuné, Alba Reche", R.drawable.moonlight, "03:45", "moonlight"),
        Cancion("CAMBIAR EL MUNDO", "Bejo, Cookin Soul", R.drawable.cambiarelmundo, "04:30", "cambiarelmundo"),
        Cancion("Guillao", "La Pantera, Juseph, Bdp Music", R.drawable.guillao, "02:50", "guillao"),
        Cancion("Ganas", "Maikel Delacalle", R.drawable.ganas, "05:15", "ganas"),
        Cancion("LEONARDO FLEXXO", "La$$ Suga', Cuki Music, MPadrums", R.drawable.leonardo, "03:10", "leonardoflexxo")
    )

    var indiceCancionActual = mutableStateOf(0)
    var cancionActual = mutableStateOf(canciones[indiceCancionActual.value])
    var player: Player? = null
    var isPlaying = mutableStateOf(false)
    var isLooping = mutableStateOf(false)
    var isShuffling = mutableStateOf(false)

    init {
        player = ExoPlayer.Builder(context).build()
        prepararCancion()
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    siguienteCancion()
                }
            }
        })
    }

    fun togglePlayPause() {
        if (isPlaying.value) {
            player?.pause()
        } else {
            player?.play()
        }
        isPlaying.value = !isPlaying.value
    }

    fun toggleLoop() {
        isLooping.value = !isLooping.value
        player?.repeatMode = if (isLooping.value) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }

    fun toggleShuffle() {
        isShuffling.value = !isShuffling.value
    }

    fun siguienteCancion() {
        if (isLooping.value) {
            prepararCancion()
            player?.play()
            isPlaying.value = true
        } else if (isShuffling.value) {
            var nextIndex: Int
            do {
                nextIndex = Random.nextInt(canciones.size)
            } while (nextIndex == indiceCancionActual.value)
            indiceCancionActual.value = nextIndex
            cancionActual.value = canciones[indiceCancionActual.value]
            prepararCancion()
            player?.play()
            isPlaying.value = true
        } else {
            indiceCancionActual.value = (indiceCancionActual.value + 1) % canciones.size
            cancionActual.value = canciones[indiceCancionActual.value]
            prepararCancion()
            player?.play()
            isPlaying.value = true
        }
    }

    fun cancionAnterior() {
        indiceCancionActual.value = (indiceCancionActual.value - 1 + canciones.size) % canciones.size
        cancionActual.value = canciones[indiceCancionActual.value]
        prepararCancion()
        player?.play()
        isPlaying.value = true
    }

    private fun prepararCancion() {
        val uri = Uri.parse("android.resource://tu.nombre.de.paquete/" + context.resources.getIdentifier(cancionActual.value.ruta, "raw", context.packageName))
        player?.setMediaItem(MediaItem.fromUri(uri))
        player?.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
    }
}
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: CancionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CancionViewModel(this)
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
    val isLooping by viewModel.isLooping
    val isShuffling by viewModel.isShuffling

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
            IconButton(onClick = { viewModel.toggleShuffle() }) {
                Icon(Icons.Default.Shuffle, contentDescription = null, tint = if (isShuffling) Color.Green else Color.Gray)
            }
            IconButton(onClick = { viewModel.cancionAnterior() }) { Icon(Icons.Default.SkipPrevious, contentDescription = null) }
            IconButton(onClick = { viewModel.togglePlayPause() },modifier = Modifier.background(shape = RoundedCornerShape(50.dp), color=Color.Blue)) {
                if (viewModel.isPlaying.value) {
                    Icon(Icons.Default.Pause, contentDescription=null)
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription=null)
                }
            }
            IconButton(onClick = { viewModel.siguienteCancion() }) { Icon(Icons.Default.SkipNext, contentDescription=null)}
            IconButton(onClick = { viewModel.toggleLoop() }) {
                Icon(Icons.Default.Repeat, contentDescription=null, tint = if (isLooping) Color.Green else Color.Gray)
            }
        }
    }
}