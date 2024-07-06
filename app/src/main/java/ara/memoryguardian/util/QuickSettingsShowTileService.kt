package ara.memoryguardian.util

import android.app.Dialog
import android.os.Build
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi
import ara.memoryguardian.work.HClipboard
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class QuickSettingsShowTileService : TileService() {

    @Inject
    lateinit var hClipboard: HClipboard

    override fun onClick() {
        super.onClick()
        Timber.d("QuickSettingsClearTileService # onClick")

        val clipboardContent = hClipboard.getContent()
        Toast.makeText(this, clipboardContent, Toast.LENGTH_LONG).show()

        // collapse the Quick Settings panel
        val dialog = Dialog(this)
        showDialog(dialog)
        dialog.hide()
    }
}
