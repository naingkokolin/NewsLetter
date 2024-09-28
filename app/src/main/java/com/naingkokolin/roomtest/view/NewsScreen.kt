package com.naingkokolin.roomtest.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.naingkokolin.roomtest.MainViewModel
import com.naingkokolin.roomtest.data.Article
import com.naingkokolin.roomtest.data.getFavoriteState
import com.naingkokolin.roomtest.data.saveFavoriteState
import kotlinx.coroutines.launch

@Composable
fun FirstScreen(
    navigateToDetail: (Article) -> Unit,
    newsState: MainViewModel.NewsState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        when {
            newsState.loading -> {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            }

            newsState.error != null -> {
                Text(text = "Error Occurred while fetching the data.", modifier = modifier.align(Alignment.Center))
            }

            else -> {
                NewsScreen(newsList = newsState.news, navigateToDetail = navigateToDetail)
            }
        }
    }
}

@Composable
fun NewsScreen(
    newsList: List<Article>,
    navigateToDetail: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(newsList) {news ->
            NewsItem(article = news, clicked = navigateToDetail)
        }
    }
}

@Composable
fun NewsItem(
    article: Article,
    clicked: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(4.dp)
            .clickable { clicked(article) }
            .border(1.dp, Color.Black, RoundedCornerShape(10)),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // image
         Image(
            painter = rememberAsyncImagePainter(model = article.urlToImage),
            contentDescription = "Image",
            Modifier.weight(1f).padding(horizontal = 2.dp).clip(RoundedCornerShape(10))
        )

        Column(
            modifier = modifier
                .weight(2f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // title text
            Text(
                text = article.title,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = modifier.padding(bottom = 8.dp)
            )

            // author text
            Text(
                text = article.author ?: "No author",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,

            )
        }
    }
}

@Composable
fun DetailScreen(
    article: Article,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val scope = rememberCoroutineScope()
    
    var favBtnColor by rememberSaveable {
        mutableStateOf(false)
    }
    
    val savedFavoriteState = getFavoriteState(context).collectAsState(initial = false)

    LaunchedEffect(key1 = savedFavoriteState.value) {
        favBtnColor = savedFavoriteState.value
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Image
            Image(
                rememberAsyncImagePainter(model = article.urlToImage),
                "image",
                Modifier
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(topEndPercent = 20, bottomStartPercent = 20))
            )

            // Title text
            Text(
                text = article.title,
                modifier = modifier.wrapContentWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // description text
            Text(
                text = article.description ?: "No description",
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            // time text
            Text(
                text = article.publishedAt,
                fontStyle = FontStyle.Italic,
                modifier = modifier.align(Alignment.End)
            )

            // author text
            Text(
                text = if (article.author != null) "Author : ${article.author}" else "No Author",
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = modifier.align(Alignment.End)
            )
        }

        FloatingActionButton(
            onClick = {
                favBtnColor = !favBtnColor
                Toast.makeText(
                    context,
                    if(favBtnColor) "Added to Favorite" else "Removed from Favorite",
                    Toast.LENGTH_SHORT
                ).show()
                scope.launch {
                    saveFavoriteState(context, favBtnColor)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Save",
                tint = if (favBtnColor) Color.Red else Color.Black
            )
        }
    }
}