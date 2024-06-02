package com.example.androidapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidapp.model.Category
import com.example.androidapp.model.Post
import com.example.androidapp.util.RequestState
import com.example.androidapp.util.convertLongToDate
import com.example.androidapp.util.decodeThumbnailImage

@Composable
fun
        PostCard(
    post: Post,
    onPostClick: (String) -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onPostClick(post._id) },
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.height(260.dp),
                model = ImageRequest
                    .Builder(context)
                    .data(
                        if (post.thumbnail.contains("http")) post.thumbnail
                        else post.thumbnail.decodeThumbnailImage()
                    )
                    .build(),
                contentDescription = "Post Thumbnail",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .alpha(0.5f),
                    text = post.date.convertLongToDate(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = post.title,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = post.subtitle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(text = Category.valueOf(post.category).name) }
                )
            }
        }
    }
}

@Composable
fun PostsCardView(
    posts: RequestState<List<Post>>,
    topMargin: Dp,
    hideMessage: Boolean = false,
    onPostClick: (String) -> Unit
    ) {
    when (posts) {
        is RequestState.Success -> {
            if (posts.data.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = topMargin)
                        .padding(horizontal = 24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = posts.data,
                        key = { post -> post._id }
                    ) { post ->
                        PostCard(
                            post = post,
                            onPostClick = onPostClick
                        )
                    }
                }
            } else {
                EmptyUi()
            }
        }

        is RequestState.Error -> {
            EmptyUi(message = posts.error.message.toString())
        }

        is RequestState.Idle -> {
            EmptyUi(hideMessage = hideMessage)
        }
        is RequestState.Loading -> {
            EmptyUi(loading = true)
        }
    }
}