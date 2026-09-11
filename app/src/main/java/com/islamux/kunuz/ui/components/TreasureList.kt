package com.islamux.kunuz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.ui.theme.LocalKunuzTextScale

@Composable
fun TreasureList(
    treasures: List<Treasure>,
    chapterById: Map<ChapterId, Chapter>,
    favorites: Set<Int>,
    fontSize: FontSize,
    showTashkeel: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenShare: (Treasure) -> Unit,
    onOpenTasbeeh: (Treasure) -> Unit,
    onSelectTag: (String) -> Unit,
    onClearFilters: () -> Unit,
    onToggleListen: (Treasure) -> Unit = {},
    listeningTreasureId: Int? = null,
    modifier: Modifier = Modifier,
    textScale: Float = LocalKunuzTextScale.current
) {
    if (treasures.isEmpty()) {
        EmptyState(onClearFilters = onClearFilters, modifier = modifier.fillMaxWidth())
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(treasures, key = { it.id }) { treasure ->
            TreasureCard(
                treasure = treasure,
                chapter = chapterById[treasure.chapterId],
                isFavorite = favorites.contains(treasure.id),
                onToggleFavorite = onToggleFavorite,
                fontSize = fontSize,
                showTashkeel = showTashkeel,
                onOpenShare = onOpenShare,
                onOpenTasbeeh = onOpenTasbeeh,
                onSelectTag = onSelectTag,
                onToggleListen = { onToggleListen(treasure) },
                isListening = listeningTreasureId == treasure.id,
                textScale = textScale
            )
        }
    }
}