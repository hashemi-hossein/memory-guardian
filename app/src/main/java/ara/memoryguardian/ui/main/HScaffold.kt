package ara.memoryguardian.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ara.memoryguardian.R
import ara.memoryguardian.ui.screen.SettingsScreen
import ara.memoryguardian.ui.screen.AppViewModel
import ara.memoryguardian.ui.screen.HomeScreen
import ara.memoryguardian.work.MemoryForegroundService
import kotlinx.coroutines.launch


@Composable
fun HScaffold(viewModel: AppViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    uiState.snackBarMessage?.let { snackBarMessage ->
        scope.launch {
            snackBarHostState.showSnackbar(snackBarMessage, withDismissAction = true)
            viewModel.emptySnackBarMessage()
        }
    }

    LaunchedEffect(uiState.isAutoCleaningEnable != null) {
        if (uiState.isAutoCleaningEnable == true) {
            MemoryForegroundService.start(context)
        }
    }

    var bottomNavigationSelectedItem by remember { mutableIntStateOf(0) }
    val bottomNavigationItemList = remember {
        listOf(
            Icons.Default.EditNote to context.getString(R.string.clipboard_editor),
            Icons.Default.Settings to context.getString(R.string.settings),
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }, bottomBar = {
        NavigationBar {
            bottomNavigationItemList.forEachIndexed { index, item ->
                NavigationBarItem(selected = bottomNavigationSelectedItem == index,
                    onClick = { bottomNavigationSelectedItem = index },
                    label = { Text(text = item.second) },
                    icon = { Icon(imageVector = item.first, contentDescription = null) })
            }
        }
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center,
        ) {
            Crossfade(targetState = bottomNavigationSelectedItem, label = "screen_transition") {
                when (it) {
                    0 -> HomeScreen(viewModel = viewModel, uiState = uiState)
                    1 -> SettingsScreen(viewModel = viewModel, uiState = uiState)
                }
            }
        }
    }
}
