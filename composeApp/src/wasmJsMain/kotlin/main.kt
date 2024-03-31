import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.linroid.pexels.App
import com.linroid.pexels.Startup

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Startup.run()
    CanvasBasedWindow("Pexels") {
        App()
    }
}
