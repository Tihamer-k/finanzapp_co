package com.tihcodes.finanzapp.co.di

import com.russhwolf.settings.Settings
import com.tihcodes.finanzapp.co.data.local.CategoryDatabase
import com.tihcodes.finanzapp.co.data.local.TransactionDatabase
import com.tihcodes.finanzapp.co.data.local.UserDatabase
import com.tihcodes.finanzapp.co.data.repository.AuthRepositoryImpl
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


expect val targetModule: Module

val sharedModule = module {
    single<UserDatabase> { UserDatabase(
        databaseDriverFactory = get(),
    ) }
    single<Settings> { Settings() }
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<CategoryDatabase> { CategoryDatabase(databaseDriverFactory = get()) }
    single<TransactionDatabase> { TransactionDatabase(databaseDriverFactory = get()) }
    single { CategoryRepository(firestore = get<FirebaseFirestore>(), categoryDatabase = get<CategoryDatabase>()) }
    single { TransactionRepository(firestore = get<FirebaseFirestore>(), transactionDatabase = get<TransactionDatabase>()) }

    viewModel { AuthViewModel(
        authRepository = AuthRepositoryImpl(
            auth = get<FirebaseAuth>(),
            database = get<FirebaseFirestore>(),
            settings = get<Settings>(),
            userDatabase = get<UserDatabase>(),
        ),
    ) }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}
