package com.tvtracker

import com.tvtracker.service.IMediaService
import com.tvtracker.service.MediaService
import com.tvtracker.ui.main.BrowseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { BrowseViewModel(get()) }
    single<IMediaService> { MediaService() }
}