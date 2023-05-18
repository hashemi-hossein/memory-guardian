package ara.memoryguardian.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ara.memoryguardian.ui.screen.home.HomeScreen
import ara.memoryguardian.ui.screen.home.HomeViewModel
import ara.memoryguardian.ui.theme.MemoryGuardianTheme

@Composable
fun Main() {
    MemoryGuardianTheme {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(viewModel = viewModel)
    }
}
