package ara.memoryguardian.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(startDestination = "home", navController = navController) {
        composable("home") {
            HScaffold(viewModel = hiltViewModel())
        }
    }
}
