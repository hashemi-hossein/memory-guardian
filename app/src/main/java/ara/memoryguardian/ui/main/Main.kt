package ara.memoryguardian.ui.main

import androidx.compose.runtime.Composable
import ara.memoryguardian.ui.screen.home.HomeScreen
import ara.memoryguardian.ui.theme.MemoryGuardianTheme

@Composable
fun Main() {
    MemoryGuardianTheme {
        HomeScreen()
    }
}