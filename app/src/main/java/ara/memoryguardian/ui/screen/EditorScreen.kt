package ara.memoryguardian.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ara.memoryguardian.R

@Composable
fun EditorScreen(
    viewModel: AppViewModel,
    uiState: AppUIState,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(onClick = viewModel::getCurrentClipboardContent) {
                Text(text = stringResource(R.string.get))
            }
            Button(onClick = viewModel::clearClipboard) {
                Text(text = stringResource(R.string.clear))
            }
            Button(onClick = viewModel::setClipboardContent) {
                Text(text = stringResource(R.string.set))
            }
        }

        Card {
            TextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = uiState.clipboardContent,
                onValueChange = viewModel::setContent,
            )
        }
    }
}
