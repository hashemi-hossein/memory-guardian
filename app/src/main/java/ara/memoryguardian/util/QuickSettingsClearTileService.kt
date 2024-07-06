package ara.memoryguardian.util

import android.app.Dialog
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import ara.memoryguardian.work.HClipboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class QuickSettingsClearTileService : TileService() {

    @Inject
    lateinit var hClipboard: HClipboard

    private lateinit var serviceScope: CoroutineScope

    override fun onStartListening() {
        super.onTileAdded()

        Timber.d("QuickSettingsClearTileService # onStartListening")
        serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    override fun onStopListening() {
        super.onTileRemoved()

        Timber.d("QuickSettingsClearTileService # onStopListening")
        serviceScope.cancel()
    }

    override fun onClick() {
        super.onClick()
        Timber.d("QuickSettingsClearTileService # onClick")

        serviceScope.launch {
            hClipboard.clear()
        }

        // collapse the Quick Settings panel
        val dialog = Dialog(this)
        showDialog(dialog)
        dialog.hide()
    }
}
