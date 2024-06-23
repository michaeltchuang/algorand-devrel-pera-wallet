package com.algorand.android.ui.staking.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.algorand.android.R
import com.algorand.android.models.Validator

@SuppressLint("ComposableNaming")
@Composable
fun validatorSearchItemIconColumn(validator: Validator) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .width(100.dp)
            .fillMaxHeight(),
    ) {
        val context = LocalContext.current
        val placeholder = R.drawable.ic_no_accounts_24
        val imageUrl = validator.avatar
        val imageRequest = ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = stringResource(R.string.validator_icon_content_description),
            contentScale = ContentScale.Crop,
            modifier =
            Modifier
                .clip(CircleShape)
                .height(50.dp)
                .width(50.dp),
        )
    }
}
