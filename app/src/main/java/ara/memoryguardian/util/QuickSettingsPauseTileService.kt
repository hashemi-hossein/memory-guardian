package ara.memoryguardian.util

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import ara.memoryguardian.work.MemoryForegroundService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class QuickSettingsPauseTileService : TileService() {

    override fun onClick() {
        super.onClick()
        Timber.d("QuickSettingsPauseTileService # onClick")

        val intent = Intent(this, MemoryForegroundService::class.java)
            .putExtra(MemoryForegroundService.PAUSE_INTENT_EXTRA, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        // collapse the Quick Settings panel
        val dialog = Dialog(this)
        showDialog(dialog)
        dialog.hide()
    }
}
