package com.islamux.kunuz.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

/**
 * Shares a treasure's formatted text through the system share sheet and
 * clipboard.
 */
class AndroidSharer(private val context: Context) {

    /**
     * Opens the system chooser for an ACTION_SEND with plain text.
     *
     * [Intent.FLAG_ACTIVITY_NEW_TASK] is always added so the call works from
     * any [Context] (including an application context); it is harmless when an
     * Activity provides the context.
     */
    fun shareViaChooser(text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(sendIntent, null)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    /** Copies [text] to the system clipboard under [label]. */
    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(ClipboardManager::class.java) ?: return
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}