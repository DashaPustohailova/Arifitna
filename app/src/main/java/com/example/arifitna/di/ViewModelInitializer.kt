package com.exapmle.focusstart.di

import com.example.arifitna.ui.profile.ProfileFragmentViewModel
import com.example.arifitna.ui.settings.SettingsViewModel
import com.example.arifitna.ui.signIn.registrationFragment.RegistrationFragment
import com.example.arifitna.ui.signIn.registrationFragment.RegistrationFragmentViewModel
import com.example.arifitna.ui.startFragment.StartFragmentViewModel
import com.example.arifitna.ui.signIn.signInFragment.SignInViewModel
import com.example.arifitna.ui.statistics.StatisticsViewModel
import com.example.arifitna.use_case.SignOutUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<StartFragmentViewModel> { StartFragmentViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel<SettingsViewModel> { SettingsViewModel(get(), get(), get()) }
    viewModel<SignInViewModel> { SignInViewModel(get()) }
    viewModel<ProfileFragmentViewModel>{ ProfileFragmentViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel<RegistrationFragmentViewModel>{ RegistrationFragmentViewModel(get()) }
    viewModel<StatisticsViewModel>{ StatisticsViewModel(get(), get()) }
}