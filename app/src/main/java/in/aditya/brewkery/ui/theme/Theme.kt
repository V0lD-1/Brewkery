package `in`.aditya.brewkery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BrewkeryColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = Color.White,
    primaryContainer = StoreBannerBg,
    onPrimaryContainer = TextDark,
    secondary = EspressoDark,
    onSecondary = Color.White,
    background = WarmBackground,
    onBackground = TextDark,
    surface = CardBackground,
    onSurface = TextDark,
    outline = BorderLight
)

@Composable
fun BrewkeryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BrewkeryColorScheme,
        typography = Typography,
        content = content
    )
}
