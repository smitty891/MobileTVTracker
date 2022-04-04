package com.tvtracker

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import org.intellij.lang.annotations.JdkConstants


class MainActivity : ComponentActivity() {

    private var openDialog by mutableStateOf(false)
    private val viewModel: BrowseViewModel by viewModel<BrowseViewModel>()
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var showFavorites by mutableStateOf(false)

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
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
            var configuration = LocalConfiguration.current

            when( configuration.orientation ) {
                // is the app in portrait mode?
                Configuration.ORIENTATION_PORTRAIT -> {
                    MainContentPortrait( imdbMediaItems, userMediaItems, listState, loading, viewModel )
                } else -> {
                    MainContentLandscape( imdbMediaItems, userMediaItems, listState, loading, viewModel )
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

    @ExperimentalComposeUiApi
    @Composable
    fun MainContentPortrait(
        imdbMediaItems: List<MediaItem>,
        userMediaItems: List<MediaItem>,
        listState: LazyListState,
        loading: Boolean,
        viewModel: BrowseViewModel
    ) {
        TvTrackerTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                if (openDialog){
                    MoviePopup(viewModel.selectedMediaItem)
                }
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

    @ExperimentalComposeUiApi
    @Composable
    fun MainContentLandscape(
        imdbMediaItems: List<MediaItem>,
        userMediaItems: List<MediaItem>,
        listState: LazyListState,
        loading: Boolean,
        viewModel: BrowseViewModel
    ) {
        TvTrackerTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                if (openDialog){
                    MoviePopup(viewModel.selectedMediaItem)
                }
                Scaffold() {
                    Row( modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize() ) {
                        Column(  ) {
                            TVTrackerMenuLandscape()
                        }
                        Column( verticalArrangement = Arrangement.spacedBy(10.dp) ) {
                            SearchBar(
                                onTextChanged = {
                                    viewModel.searchTxt = it
                                },
                                onSearchClicked = {
                                    viewModel.searchImdb(listState)
                                }
                            )
                            LoadingSpinner(isDisplayed = loading)

                            if (showFavorites) {
                                UserMediaItemColumn(userMediaItems)
                            } else {
                                ImdbMediaItemColumn(listState, imdbMediaItems)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun MoviePopup (mediaItem: MediaItem){
        if (mediaItem.imageUrl == "N/A") {
            mediaItem.imageUrl = "https://i.imgur.com/N6EvlmG.png"
        }

        Dialog(
            onDismissRequest = {
                openDialog = false
            }, properties = DialogProperties(usePlatformDefaultWidth = false)
        ){
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

                backgroundColor = Color(0xFF0D47A1),
                contentColor = Color(0xFFFFFDE7),
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column() {


                Column() {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0E0E0)), horizontalArrangement = Arrangement.SpaceBetween) {
                        //Close Button
                        Button( onClick = { openDialog = false}, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), elevation = ButtonDefaults.elevation(0.dp, 0.dp)) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(ButtonDefaults.IconSize + 16.dp),
                                tint = Color.Black
                            )
                        }
                        FavoritesButton(mediaItem = mediaItem)
                    }
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Column(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        //Title
                        Text(text = mediaItem.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column() {
                            Image(painter = rememberImagePainter(mediaItem.imageUrl),
                                contentDescription = (mediaItem.title + " Poster"),
                                modifier = Modifier
                                    .size(140.dp, 200.dp)
                                    .padding(5.dp))
                        }
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Released: ")}
                                    append(mediaItem.releaseDate)
                                })
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Rating: ")}
                                    append(mediaItem.rated)
                                })
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Genre: ")}
                                    append(mediaItem.genre)
                                })
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Reception: ")}
                                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)){append((mediaItem.imdbRating + "/10"))}
                                })
                        }
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)){
                        Text(text = "Plot", fontSize = 24.sp)
                        Text(text = mediaItem.plot)
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)){
                        Text(text = "Cast & Crew", fontSize = 24.sp)
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Directed By: ")}
                                append(mediaItem.directors)
                            })
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Written By: ")}
                                append(mediaItem.writers)
                            })
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Cast: ")}
                                append(mediaItem.actors)
                            })
                    }
                }
            }
            }
        }
    }


    @Composable
    fun TVTrackerMenuLandscape() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = CenterHorizontally
        ) {
            Row( modifier = Modifier.padding( 20.dp ) ) {
                Text(text = "TV Tracker", fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
            }
            Row( modifier = Modifier.padding(15.dp) ) {
                Button(onClick = {
                    showFavorites = false
                }, content = { Text("Browse") })
            }
            Row( modifier = Modifier.padding(15.dp) ) {
                Button(onClick = {
                    if (firebaseUser == null) {
                        signIn()
                    } else {
                        showFavorites = true
                    }
                }, content = { Text("Favorites") })
            }
            Row( modifier = Modifier.padding(15.dp) ) {
                Button(onClick = {
                    val share = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check this out")

                        // (Optional) Here we're setting the title of the content
                        putExtra(Intent.EXTRA_TITLE, "See a preview")

                        // (Optional) Here we're passing a content URI to an image to be displayed
                        // data = contentUri
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }, null)
                    startActivity(share)
                }, content = { Text( "Share") })
            }   
        }
    }

    @Composable
    fun TVTrackerMenu() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Row {
                Text(text = "TV Tracker", fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp))
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = {
                        val share = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check this out")

                            // (Optional) Here we're setting the title of the content
                            putExtra(Intent.EXTRA_TITLE, "See a preview")

                            // (Optional) Here we're passing a content URI to an image to be displayed
                            // data = contentUri
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }, null)
                        startActivity(share)
                    }, content = { Text( "Share") })
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
                //TVTrackerMenu()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = text,
            onValueChange = {
                text = it
                onTextChanged(it.text)
            },
            singleLine = true,

            label = {Text(text= "Search for a Movie or TV Show", color= Color.White)},
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
        LazyColumn() {
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
                            if (mediaItem.id.isEmpty()) {
                                viewModel.getMediaItemDetails()
                            }
                            openDialog = true
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
            Box(contentAlignment = Alignment.TopEnd){
                Column(
                    Modifier
                        .fillMaxWidth().fillMaxHeight()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Text(mediaItem.title, textAlign = TextAlign.Center)
                    Text(mediaItem.year, textAlign = TextAlign.Center)
                }
                FavoritesButton(mediaItem = mediaItem)
            }
        }
    }

    @Composable
    fun FavoritesButton(mediaItem: MediaItem) {
        val favIconClickHandler = {
            viewModel.selectedMediaItem = mediaItem
            if (mediaItem.id.isEmpty()) { //if we don't have it in our DB, save it
                viewModel.saveMediaItem()
            } else {
                viewModel.deleteMediaItem() //if it's in our db, that means it's the user toggling to delete it
                openDialog = false;
            }
        }

        val favIconColor =
            if (mediaItem.id.isEmpty()) {
                Color.Black
            } else {
                Color.Yellow
            }

        //Favorites Button
        Button(onClick = favIconClickHandler, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), elevation = ButtonDefaults.elevation(0.dp, 0.dp)) {
            Icon(
                Icons.Outlined.Star,
                contentDescription = "Favorite",
                modifier = Modifier.size(ButtonDefaults.IconSize + 16.dp),
                tint = favIconColor
            )
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
