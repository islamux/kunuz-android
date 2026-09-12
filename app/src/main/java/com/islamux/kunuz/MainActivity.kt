package com.islamux.kunuz

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.islamux.kunuz.data.DailyTasksRepository
import com.islamux.kunuz.data.FavoritesRepository
import com.islamux.kunuz.data.SettingsRepository
import com.islamux.kunuz.data.TreasuresRepository
import com.islamux.kunuz.data.dataStore
import com.islamux.kunuz.ui.KunuzApp
import com.islamux.kunuz.ui.KunuzViewModel
import com.islamux.kunuz.ui.theme.KunuzTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val viewModel by viewModels<KunuzViewModel> {
        KunuzViewModelFactory(application as Application, scope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KunuzTheme {
                KunuzApp(viewModel = viewModel)
            }
        }
    }
}

private class KunuzViewModelFactory(
    private val application: Application,
    private val scope: CoroutineScope
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return KunuzViewModel(
            favoritesRepository = FavoritesRepository(application.dataStore),
            dailyTasksRepository = DailyTasksRepository(application.dataStore),
            settingsRepository = SettingsRepository(application.dataStore),
            treasuresRepository = TreasuresRepository(application),
            scope = scope
        ) as T
    }
}