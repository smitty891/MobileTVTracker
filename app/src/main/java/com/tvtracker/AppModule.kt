package com.tvtracker

import com.tvtracker.ui.main.BrowseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { BrowseViewModel() }
}