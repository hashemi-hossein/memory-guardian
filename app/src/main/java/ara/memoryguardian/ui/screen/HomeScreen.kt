package ara.memoryguardian.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ara.memoryguardian.R
import ara.memoryguardian.ui.screen.home.AppUIState
import ara.memoryguardian.ui.screen.home.AppViewModel

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    uiState: AppUIState,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Button(onClick = viewModel::clearClipboard) {
            Text(text = stringResource(R.string.clear_clipboard_now))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 7.dp))

        Button(onClick = viewModel::getCurrentClipboardContent) {
            Text(text = stringResource(R.string.get_current_clipboard_content))
        }

        Card {
            SelectionContainer(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = uiState.clipboardContent)
            }
        }
    }
}
