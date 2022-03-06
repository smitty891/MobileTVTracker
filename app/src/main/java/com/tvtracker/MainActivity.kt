package com.tvtracker

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.tvtracker.dto.MediaItem
import com.tvtracker.ui.main.BrowseViewModel
import com.tvtracker.ui.theme.TvTrackerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.tvtracker.dto.User


class MainActivity : ComponentActivity() {

    private val viewModel: BrowseViewModel by viewModel()
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var showFavorites by mutableStateOf(false)

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) {
            res -> this.signInResult(res)
    }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseUser?.let {
            viewModel.user = User(it.uid, it.displayName)
            viewModel.listenToUserMediaItems()
        }

        setContent {
            val imdbMediaItems by viewModel.imdbMediaItems.observeAsState(initial = emptyList())
            val userMediaItems by viewModel.userMediaItems.observeAsState(initial = emptyList())
            val listState = rememberLazyListState()
            val loading = viewModel.loading

            TvTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            Column {
                                Row {
                                    TVTrackerMenu()
                                }
                                Row {
                                    SearchBar(
                                        onTextChanged = {
                                            viewModel.searchTxt = it
                                        },
                                        onSearchClicked = {
                                            viewModel.searchImdb(listState)
                                        }
                                    )
                                }
                            }
                        },
                        content = {
                            LoadingSpinner(isDisplayed = loading)

                            if (showFavorites) {
                                UserMediaItemColumn(userMediaItems)
                            } else {
                                ImdbMediaItemColumn(listState, imdbMediaItems)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                viewModel.user = User(it.uid, it.displayName)
                viewModel.saveUser()
                viewModel.listenToUserMediaItems()
            }
        } else {
            Log.e("Error Signing In", "Error: ${response?.error?.errorCode}")
        }
    }

    @Composable
    fun TVTrackerMenu() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Row {
                Text(text = "TV Tracker", fontSize = 35.sp, fontWeight = FontWeight.Bold)
            }
            Row {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = {
                        showFavorites = false
                    }, content = { Text("Browse") })
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = {
                        if (firebaseUser == null) {
                            signIn()
                        } else {
                            showFavorites = true
                        }
                    }, content = { Text("Favorites") })
                }
            }
        }
    }

    @ExperimentalComposeUiApi
    @Preview(name = "Light Mode", showBackground = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
    @Composable
    fun TopBarPreview() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            Row {
                TVTrackerMenu()
            }
            Row {
                SearchBar(
                    onTextChanged = {

                    },
                    onSearchClicked = {

                    }
                )
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

        TextField(
            modifier = Modifier.fillMaxWidth(),
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

    @Composable
    fun UserMediaItemColumn(mediaItems: List<MediaItem>) {
        LazyColumn {
            itemsIndexed(mediaItems) { index, item ->
                MediaItemRow(item)
            }
        }
    }

    @Composable
    fun ImdbMediaItemColumn(listState: LazyListState, mediaItems: List<MediaItem>) {
        LazyColumn(state = listState) {
            itemsIndexed(mediaItems) { index, item ->
                viewModel.onChangeScrollPosition(index)
                if ((index + 1) >= (viewModel.page * viewModel.PAGE_SIZE) && !viewModel.loading) {
                    viewModel.nextPage()
                }
                MediaItemRow(item)
            }
        }
    }

    @Composable
    fun MediaItemRow(mediaItem: MediaItem) {

        if (mediaItem.imageUrl == "N/A") {
            mediaItem.imageUrl = "https://i.imgur.com/N6EvlmG.png"
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(210.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            viewModel.selectedMediaItem = mediaItem
                            viewModel.getMediaItemDetails()
                            //viewModel.saveMediaItem()
                        }
                    )
                }
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

    @Composable
    fun LoadingSpinner(isDisplayed: Boolean) {
        if (isDisplayed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}
