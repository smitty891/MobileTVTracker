package com.tvtracker

import android.content.res.Configuration
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tvtracker.ui.theme.TvTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TVTrackerMenu("Android")
                }
            }
        }
    }
}

@Composable
fun TVTrackerMenu(name: String) {

    var context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Row{
            Text(text = "TV Tracker", fontSize = 32.sp)
        }
        Row{
            Column(modifier = Modifier.padding(16.dp)){
                Button(onClick = {

                },
                    content = {Text(text = "Browse")})
            }
            Column(modifier = Modifier.padding(16.dp)){
                Button(onClick = {

                },
                    content = {Text(text = "Favourites")})
            }
        }
        Row{
            SearchView(context)
        }
        Row{

        }
    }
}

@Preview(name="Light Mode", showBackground=true)
@Preview(uiMode= Configuration.UI_MODE_NIGHT_YES, showBackground = true, name="Dark Mode")
@Composable
fun DefaultPreview() {
    TvTrackerTheme {
        Surface(color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            TVTrackerMenu("Android")
        }
    }
}