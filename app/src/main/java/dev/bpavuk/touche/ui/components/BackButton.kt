package dev.bpavuk.touche.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.R

@Composable
fun BackButton(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(R.drawable.arrow_back_24px),
        contentDescription = "Back", // TODO: replace with string res
        modifier = Modifier
            .padding(16.dp)
            .clip(CircleShape)
            .clickable(onClick = onBackPressed)
        then modifier
    )
}
