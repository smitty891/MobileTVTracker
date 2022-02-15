package com.tvtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tvtracker.dto.MediaItem
import com.tvtracker.ui.main.BrowseViewModel
import com.tvtracker.ui.theme.TvTrackerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BrowseViewModel by viewModel<BrowseViewModel>()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TvTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val listState = rememberLazyListState()
                    Scaffold(
                        topBar = {
                            SearchBar(
                                onTextChanged = {
                                    viewModel.searchTxt = it
                                },
                                onSearchClicked = {
                                    viewModel.search(listState)
                                }
                            )
                         },
                        content = {
                            LazyColumn(state = listState) {
                                itemsIndexed(viewModel.mediaItems) { index, item ->
                                    viewModel.onChangeScrollPosition(index)
                                    if((index + 1) >= (viewModel.page * viewModel.PAGE_SIZE) && !viewModel.loading){
                                        viewModel.nextPage()
                                    }
                                    MediaItemRow(item)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    onTextChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onTextChanged(it.text)
            },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSearchClicked()
                        keyboardController?.hide()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked()
                    keyboardController?.hide()
                }
            )

        )
    }
}

@Composable
fun MediaItemRow(mediaItem: MediaItem) {

    if(mediaItem.imageUrl == "N/A"){
        mediaItem.imageUrl = "https://i.imgur.com/N6EvlmG.png"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(210.dp)
    ) {
        Image(
            painter = rememberImagePainter(mediaItem.imageUrl),
            contentDescription = "Media Poster",
            modifier = Modifier
                .size(140.dp, 200.dp)
                .padding(5.dp),
            alignment = Alignment.CenterStart,
            contentScale = ContentScale.FillBounds
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(mediaItem.title)
            Text(mediaItem.year)
        }
    }
}