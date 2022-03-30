package com.exapmle.focusstart.di

import com.example.arifitna.ui.Settings.SettingsFragment
import com.example.arifitna.ui.Settings.SettingsViewModel
import com.example.arifitna.ui.StartFragment.StartFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<StartFragmentViewModel> { StartFragmentViewModel() }
    viewModel<SettingsViewModel> { SettingsViewModel(get(), get(), get()) }
}