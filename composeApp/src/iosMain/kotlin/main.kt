import androidx.compose.ui.window.ComposeUIViewController
import com.linroid.pexels.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
