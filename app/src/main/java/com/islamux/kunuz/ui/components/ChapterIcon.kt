package com.islamux.kunuz.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.Coins
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.HeartHandshake
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.ScrollText
import com.composables.icons.lucide.ShieldCheck
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Sun
import com.islamux.kunuz.data.model.ChapterIconName

@Composable
fun ChapterIcon(
    icon: ChapterIconName,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = icon.imageVector(),
        contentDescription = icon.name,
        modifier = modifier.testTag("chapterIcon")
    )
}

private fun ChapterIconName.imageVector(): ImageVector = when (this) {
    ChapterIconName.Sun -> Lucide.Sun
    ChapterIconName.Compass -> Lucide.Compass
    ChapterIconName.Sparkles -> Lucide.Sparkles
    ChapterIconName.ShieldCheck -> Lucide.ShieldCheck
    ChapterIconName.BookOpen -> Lucide.BookOpen
    ChapterIconName.HeartHandshake -> Lucide.HeartHandshake
    ChapterIconName.Coins -> Lucide.Coins
    ChapterIconName.Moon -> Lucide.Moon
    ChapterIconName.ScrollText -> Lucide.ScrollText
}