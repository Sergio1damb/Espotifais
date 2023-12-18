package com.example.espotifais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.espotifais.ui.theme.EspotifaisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EspotifaisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayer()
                }
            }
        }
    }
}

@Composable
fun MusicPlayer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text("Now Playing: Moonlight", style = MaterialTheme.typography.headlineLarge)
            Text("Cruz Cafune & Alba Reche", style = MaterialTheme.typography.titleMedium)
        }
        Image(painterResource(id = R.drawable.moonlight), contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.weight(4f).fillMaxSize().padding(16.dp).clip(RoundedCornerShape(8.dp)))
        Slider(value = 0.02f, onValueChange = {}, valueRange = 0f..1f, modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("00:00")
            Text("03:45")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = { }) { Icon(Icons.Default.Shuffle, contentDescription = null) }
            IconButton(onClick = { }) { Icon(Icons.Default.SkipPrevious, contentDescription = null) }
            IconButton(onClick = { }) { Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(48.dp).background(Color.Blue, RoundedCornerShape(24.dp))) }
            IconButton(onClick = { }) { Icon(Icons.Default.SkipNext, contentDescription = null) }
            IconButton(onClick = { }) { Icon(Icons.Default.Repeat, contentDescription = null) }
        }
    }
}
