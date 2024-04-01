import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.linroid.pexels.App
import com.linroid.pexels.Startup
import java.awt.Dimension

fun main() {
    Startup.run()

    application {
        Window(
            title = "Pexels",
            state = rememberWindowState(width = 1080.dp, height = 800.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(350, 600)
            App()
        }
    }
}