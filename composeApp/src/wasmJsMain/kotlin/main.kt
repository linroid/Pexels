import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.linroid.pexels.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Pexels") {
        App()
    }
}
