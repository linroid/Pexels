import androidx.compose.ui.window.ComposeUIViewController
import com.linroid.pexels.App
import com.linroid.pexels.Startup
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }

fun onStartup() {
	Startup.run()
}