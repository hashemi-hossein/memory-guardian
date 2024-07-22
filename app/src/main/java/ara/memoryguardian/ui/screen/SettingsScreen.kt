package ara.memoryguardian.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import ara.memoryguardian.R
import ara.memoryguardian.work.MemoryForegroundService
import timber.log.Timber

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    uiState: AppUIState,
) {
    val context = LocalContext.current

    val notificationPermissionRequestContract = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.toggleNotification(true)
        } else {
            Timber.e("POST_NOTIFICATIONS permission have not granted")
            viewModel.showSnackBar(context.getString(R.string.the_permission_is_needed_to_show_notifications))
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(text = stringResource(R.string.small_popup))
                Switch(
                    checked = uiState.isSmallPopupEnable,
                    onCheckedChange = viewModel::toggleSmallPopup,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(text = stringResource(R.string.notification))
                Switch(
                    checked = uiState.isNotificationEnable,
                    onCheckedChange = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                    != PackageManager.PERMISSION_GRANTED)
                        ) {
                            Timber.d("launch notification permission requester")
                            notificationPermissionRequestContract.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.toggleNotification(it)
                        }
                    }
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 7.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = stringResource(R.string.auto_clear_clipboard))
            Switch(
                checked = uiState.isAutoCleaningEnable ?: false,
                onCheckedChange = {
                    viewModel.toggleAutoClearing(it)
                    if (it) MemoryForegroundService.start(context)
                    else MemoryForegroundService.stop(context)
                }
            )
        }
        OutlinedTextField(
            value = uiState.autoCleaningIntervalSecond,
            onValueChange = viewModel::changeInterval,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = stringResource(R.string.interval_seconds)) },
            isError = viewModel.isIntervalError(),
            supportingText = { if (viewModel.isIntervalError()) Text(text = stringResource(R.string.please_enter_a_number_that_is_1_or_greater)) },
        )
    }
}