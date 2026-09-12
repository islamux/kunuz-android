package com.islamux.kunuz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.ListChecks
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.TabId
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.ui.components.AboutDialog
import com.islamux.kunuz.ui.components.ActiveFiltersBar
import com.islamux.kunuz.ui.components.ChapterChips
import com.islamux.kunuz.ui.components.ChaptersGrid
import com.islamux.kunuz.ui.components.DailyTreasureModal
import com.islamux.kunuz.ui.components.FavoritesHeader
import com.islamux.kunuz.ui.components.HeroBanner
import com.islamux.kunuz.ui.components.TopBar
import com.islamux.kunuz.ui.components.TreasureList
import com.islamux.kunuz.ui.tasbeeh.TasbeehScreen
import com.islamux.kunuz.ui.theme.KunuzBorder
import com.islamux.kunuz.ui.theme.KunuzPrimary
import com.islamux.kunuz.ui.theme.KunuzPrimaryDark
import com.islamux.kunuz.ui.theme.KunuzSurface
import com.islamux.kunuz.ui.theme.KunuzText

@Composable
fun KunuzApp(viewModel: KunuzViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val treasures by viewModel.filteredTreasures.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            KunuzBottomNav(
                tab = uiState.tab,
                favoritesCount = uiState.favorites.size,
                onSelect = viewModel::selectTab
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    fontSize = uiState.fontSize,
                    totalTreasures = viewModel.totalTreasures,
                    showTashkeel = uiState.showTashkeel,
                    searchQuery = uiState.searchQuery,
                    onCycleFontSize = viewModel::cycleFontSize,
                    onToggleTashkeel = { viewModel.setShowTashkeel(!uiState.showTashkeel) },
                    onOpenDaily = viewModel::openDailyModal,
                    onOpenRandom = viewModel::openDailyModal,
                    onOpenAbout = viewModel::openAboutModal,
                    onSearchChange = viewModel::setSearchQuery,
                    onClearSearch = { viewModel.setSearchQuery("") },
                    onBrandClick = { viewModel.selectTab(TabId.ALL) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (uiState.tab) {
                        TabId.ALL -> AllTab(
                            state = uiState,
                            treasures = treasures,
                            viewModel = viewModel
                        )

                        TabId.CHAPTERS -> ChaptersGrid(
                            chapters = viewModel.chapters,
                            chapterCounts = viewModel.chapterCounts,
                            totalTreasures = viewModel.totalTreasures,
                            selectedChapter = uiState.selectedChapter,
                            onSelectChapter = viewModel::selectChapter
                        )

                        TabId.FAVORITES -> FavoritesTab(
                            state = uiState,
                            treasures = treasures,
                            viewModel = viewModel
                        )

                        TabId.TASBEEH -> TasbeehPlaceholderTab()

                        TabId.CHECKLIST -> ComingSoonTab()
                    }
                }
            }
        }
    }

    if (uiState.showDailyModal) {
        DailyTreasureModal(
            treasure = viewModel.dailyTreasure,
            chapter = viewModel.chapterById[viewModel.dailyTreasure.chapterId],
            isFavorite = viewModel.dailyTreasure.id in uiState.favorites,
            showTashkeel = uiState.showTashkeel,
            onToggleFavorite = viewModel::toggleFavorite,
            onOpenShare = viewModel::openShare,
            onOpenChecklist = {
                viewModel.closeDailyModal()
                viewModel.openChecklist()
            },
            onClose = viewModel::closeDailyModal
        )
    }

    if (uiState.showAboutModal) {
        AboutDialog(onClose = viewModel::closeAboutModal)
    }

    if (uiState.showChecklist) {
        ComingSoonDialog(
            title = "الورد اليومي",
            body = "جدول الورد اليومي سيكون متاحاً في الخطوات القادمة.",
            onClose = viewModel::closeChecklist
        )
    }

    uiState.shareTreasure?.let {
        ComingSoonDialog(
            title = "مشاركة الكنز",
            body = "بطاقة المشاركة وتحميلها ستكون متاحة في الخطوات القادمة.",
            onClose = viewModel::closeShare
        )
    }

    uiState.tasbeehTreasure?.let {
        TasbeehScreen(treasure = it, onClose = viewModel::closeTasbeeh)
    }
}

@Composable
private fun AllTab(
    state: KunuzUiState,
    treasures: List<Treasure>,
    viewModel: KunuzViewModel
) {
    val hasFilters = state.searchQuery.isNotBlank() ||
        state.selectedChapter != null ||
        state.selectedTag != null

    Column(modifier = Modifier.fillMaxSize()) {
        if (!hasFilters) {
            HeroBanner(
                totalTreasures = viewModel.totalTreasures,
                chaptersCount = viewModel.chapters.size,
                onOpenDaily = viewModel::openDailyModal,
                onOpenChecklist = viewModel::openChecklist
            )
        }

        ChapterChips(
            chapters = viewModel.chapters,
            chapterCounts = viewModel.chapterCounts,
            totalCount = viewModel.totalTreasures,
            selectedChapter = state.selectedChapter,
            onSelect = { id -> viewModel.selectChapter(id, forceAllTab = false) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .testTag("chapter-chips")
        )

        if (hasFilters) {
            ActiveFiltersBar(
                selectedChapter = state.selectedChapter,
                selectedTag = state.selectedTag,
                searchQuery = state.searchQuery,
                resultCount = treasures.size,
                chapters = viewModel.chapters,
                onClearChapter = { viewModel.selectChapter(null, forceAllTab = false) },
                onClearSearch = { viewModel.setSearchQuery("") },
                onClearTag = { viewModel.selectTag(null) },
                onClearAll = viewModel::clearFilters,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        TreasureList(
            treasures = treasures,
            chapterById = viewModel.chapterById,
            favorites = state.favorites,
            fontSize = state.fontSize,
            showTashkeel = state.showTashkeel,
            onToggleFavorite = viewModel::toggleFavorite,
            onOpenShare = viewModel::openShare,
            onOpenTasbeeh = viewModel::openTasbeeh,
            onSelectTag = viewModel::selectTag,
            onClearFilters = viewModel::clearFilters,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun FavoritesTab(
    state: KunuzUiState,
    treasures: List<Treasure>,
    viewModel: KunuzViewModel
) {
    val hasFilters = state.searchQuery.isNotBlank() ||
        state.selectedChapter != null ||
        state.selectedTag != null

    Column(modifier = Modifier.fillMaxSize()) {
        FavoritesHeader(
            shownCount = treasures.size,
            totalFavorites = state.favorites.size,
            onClearAll = viewModel::clearFavorites,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ChapterChips(
            chapters = viewModel.chapters,
            chapterCounts = viewModel.chapterCounts,
            totalCount = viewModel.totalTreasures,
            selectedChapter = state.selectedChapter,
            onSelect = { id -> viewModel.selectChapter(id, forceAllTab = false) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .testTag("chapter-chips")
        )

        if (hasFilters) {
            ActiveFiltersBar(
                selectedChapter = state.selectedChapter,
                selectedTag = state.selectedTag,
                searchQuery = state.searchQuery,
                resultCount = treasures.size,
                chapters = viewModel.chapters,
                onClearChapter = { viewModel.selectChapter(null, forceAllTab = false) },
                onClearSearch = { viewModel.setSearchQuery("") },
                onClearTag = { viewModel.selectTag(null) },
                onClearAll = viewModel::clearFilters,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        TreasureList(
            treasures = treasures,
            chapterById = viewModel.chapterById,
            favorites = state.favorites,
            fontSize = state.fontSize,
            showTashkeel = state.showTashkeel,
            onToggleFavorite = viewModel::toggleFavorite,
            onOpenShare = viewModel::openShare,
            onOpenTasbeeh = viewModel::openTasbeeh,
            onSelectTag = viewModel::selectTag,
            onClearFilters = viewModel::clearFilters,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private data class NavItem(
    val tab: TabId,
    val label: String,
    val icon: ImageVector
)

private val NAV_ITEMS = listOf(
    NavItem(TabId.ALL, "الكنوز", Lucide.LayoutGrid),
    NavItem(TabId.CHAPTERS, "الأبواب", Lucide.BookOpen),
    NavItem(TabId.FAVORITES, "المفضلة", Lucide.Bookmark),
    NavItem(TabId.TASBEEH, "المسبحة", Lucide.Compass),
    NavItem(TabId.CHECKLIST, "الورد اليومي", Lucide.ListChecks)
)

@Composable
private fun KunuzBottomNav(
    tab: TabId,
    favoritesCount: Int,
    onSelect: (TabId) -> Unit
) {
    NavigationBar(
        containerColor = KunuzSurface,
        tonalElevation = 0.dp,
        modifier = Modifier.testTag("bottom-nav")
    ) {
        NAV_ITEMS.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.testTag("nav-${item.tab.name}"),
                selected = tab == item.tab,
                onClick = { onSelect(item.tab) },
                icon = {
                    if (item.tab == TabId.FAVORITES && favoritesCount > 0) {
                        BadgedBox(badge = { Badge { Text("$favoritesCount") } }) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KunuzPrimaryDark,
                    selectedTextColor = KunuzPrimaryDark,
                    indicatorColor = KunuzPrimary.copy(alpha = 0.12f),
                    unselectedIconColor = Color(0xFF6B7280),
                    unselectedTextColor = Color(0xFF6B7280)
                )
            )
        }
    }
}

@Composable
private fun TasbeehPlaceholderTab(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tasbeeh-placeholder-tab"),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, KunuzBorder)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Lucide.Compass,
                    contentDescription = null,
                    tint = KunuzPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "المسبحة والعداد التفاعلي",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KunuzText
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "افتح كنزاً واضغط على زر المسبحة لبدء العدّ.",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun ComingSoonTab(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("coming-soon-tab"),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, KunuzBorder)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Lucide.Sparkles,
                    contentDescription = null,
                    tint = KunuzPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "قريباً",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KunuzText
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "هذه الواجهة قيد الإنشاء في الخطوات القادمة من التطوير.",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun ComingSoonDialog(title: String, body: String, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .testTag("coming-soon-dialog"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, KunuzBorder)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Lucide.Sparkles,
                    contentDescription = null,
                    tint = KunuzPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KunuzText
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = body,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "قريباً",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )
                Spacer(Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .clickable(onClick = onClose)
                        .testTag("coming-soon-close"),
                    shape = RoundedCornerShape(10.dp),
                    color = KunuzPrimary
                ) {
                    Text(
                        text = "حسناً",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 9.dp)
                    )
                }
            }
        }
    }
}