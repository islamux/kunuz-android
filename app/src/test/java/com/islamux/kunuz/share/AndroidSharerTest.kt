package com.islamux.kunuz.share

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AndroidSharerTest {

    @Test
    fun `shareViaChooser launches an ACTION_CHOOSER wrapping an ACTION_SEND`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

        AndroidSharer(activity).shareViaChooser("نص المشاركة")

        val chooser = shadowOf(activity).nextStartedActivity
        assertNotNull("expected startActivity to be recorded", chooser)
        assertEquals(Intent.ACTION_CHOOSER, chooser?.action)

        val send = chooser?.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        assertNotNull("expected an ACTION_SEND intent inside the chooser", send)
        assertEquals(Intent.ACTION_SEND, send?.action)
        assertEquals("text/plain", send?.type)
        assertEquals("نص المشاركة", send?.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun `copyToClipboard stores text and label in the primary clip`() {
        val context = RuntimeEnvironment.getApplication()

        AndroidSharer(context).copyToClipboard("نص النسخ", "الكنز ١")

        val clipboard = context.getSystemService(ClipboardManager::class.java)
        val clip = clipboard?.primaryClip
        assertNotNull(clip)
        assertEquals(1, clip?.itemCount)
        assertEquals("نص النسخ", clip?.getItemAt(0)?.text?.toString())
        assertEquals("الكنز ١", clip?.description?.label?.toString())
    }
}