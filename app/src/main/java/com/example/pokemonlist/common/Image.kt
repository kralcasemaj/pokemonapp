package com.example.pokemonlist.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun LoadImageFromResId(
    imageResId: Int,
    tint: Color? = null,
    opacity: Float = 1.0f,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier,
        contentDescription = contentDescription,
        painter = painterResource(id = imageResId),
        colorFilter = tint?.let { color ->
            ColorFilter.tint(
                color = color.copy(alpha = opacity),
                blendMode = BlendMode.SrcIn
            )
        }
    )
}

@Composable
fun LoadImageFromSvgUrl(
    svgImageUrl: String,
    tint: Color? = null,
    opacity: Float = 1.0f,
    contentDescription: String? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(svgImageUrl)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = contentDescription,
        modifier = Modifier.alpha(opacity),
        colorFilter = tint?.let { color ->
            ColorFilter.tint(
                color = color.copy(alpha = opacity),
                blendMode = BlendMode.SrcIn
            )
        }
    )
}
