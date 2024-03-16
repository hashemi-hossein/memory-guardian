package ara.memoryguardian

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ara.memoryguardian.work.HClipboard
import ara.memoryguardian.work.MemoryForegroundService
import ara.note.domain.usecase.userpreferences.ReadUserPreferencesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var readUserPreferencesUseCase: ReadUserPreferencesUseCase

    @Inject
    lateinit var hClipboard: HClipboard

    private val job: Job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("BootReceiver -- onReceive -- intent?.action=${intent?.action}")

        if (context != null && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            coroutineScope.launch {
                if (readUserPreferencesUseCase().isAutoCleaningEnable) {
//                    hClipboard.toggleAutoClearing(true)
                    MemoryForegroundService.start(context)
                }
            }
        }
    }
}
