package com.example.pokemonlist.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonlist.R
import com.example.pokemonlist.ui.appFontFamily

data class TypeLabelMetrics(
    val cornerRadius: Int,
    val fontSize: Int,
    val verticalPadding: Int,
    val horizontalPadding: Int,
    val elementSpacing: Int
) {
    companion object {
        val SMALL = TypeLabelMetrics(24, 9, 3, 8, 8)
        val MEDIUM = TypeLabelMetrics(24, 12, 4, 12, 8)
    }
}

@Composable
fun PokemonTypeLabels(types: List<String>?, metrics: TypeLabelMetrics) {
    types?.forEach {
        Surface(
            color = Color(0x38FFFFFF),
            shape = RoundedCornerShape(metrics.cornerRadius)
        ) {
            PokemonTypeLabel(it, metrics)
        }
        Spacer(
            modifier = Modifier
                .width(metrics.elementSpacing.dp)
                .height(metrics.elementSpacing.dp)
        )
    }
}

@Composable
fun PokemonTypeLabel(text: String, metrics: TypeLabelMetrics) {
    Text(
        text = text,
        modifier = Modifier.padding(
            start = metrics.horizontalPadding.dp,
            end = metrics.horizontalPadding.dp,
            top = metrics.verticalPadding.dp,
            bottom = metrics.verticalPadding.dp
        ),
        style = TextStyle(
            fontFamily = appFontFamily,
            fontSize = metrics.fontSize.sp,
            color = colorResource(R.color.white_1000)
        )
    )
}
