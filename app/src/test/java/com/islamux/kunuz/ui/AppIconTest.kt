package com.islamux.kunuz.ui

import android.content.pm.ApplicationInfo
import android.graphics.drawable.AdaptiveIconDrawable
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class AppIconTest {

    @Test
    fun `launcher icon and round icon are declared in the manifest`() {
        val app = RuntimeEnvironment.getApplication()
        val roundIconField = ApplicationInfo::class.java.getField("roundIconRes")
        assertNotEquals(0, app.applicationInfo.icon)
        assertNotEquals(0, roundIconField.getInt(app.applicationInfo))
    }

    @Test
    fun `adaptive launcher icon resolves to a drawable`() {
        val app = RuntimeEnvironment.getApplication()
        val resolved = app.applicationInfo.loadIcon(app.packageManager) as AdaptiveIconDrawable
        assertTrue(resolved.foreground != null && resolved.background != null)
    }
}
