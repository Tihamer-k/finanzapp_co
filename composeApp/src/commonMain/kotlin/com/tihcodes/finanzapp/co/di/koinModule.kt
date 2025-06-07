package com.tihcodes.finanzapp.co.di

import com.russhwolf.settings.Settings
import com.tihcodes.finanzapp.co.data.local.CategoryDatabase
import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.LocalCourseTrackingDataSource
import com.tihcodes.finanzapp.co.data.local.LocalNotification
import com.tihcodes.finanzapp.co.data.local.LocalRewardsDataSource
import com.tihcodes.finanzapp.co.data.local.TransactionDatabase
import com.tihcodes.finanzapp.co.data.local.UserDatabase
import com.tihcodes.finanzapp.co.data.remote.FirebaseCourseTrackingDataSource
import com.tihcodes.finanzapp.co.data.remote.FirebaseRewardsDataSource
import com.tihcodes.finanzapp.co.data.remote.RemoteNotification
import com.tihcodes.finanzapp.co.data.repository.AuthRepositoryImpl
import com.tihcodes.finanzapp.co.data.repository.CourseTrackingRepositoryImpl
import com.tihcodes.finanzapp.co.data.repository.NotificationRepositoryImpl
import com.tihcodes.finanzapp.co.data.repository.RewardsRepositoryImpl
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.CourseTrackingRepository
import com.tihcodes.finanzapp.co.domain.repository.NotificationRepository
import com.tihcodes.finanzapp.co.domain.repository.RewardsRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.viewmodel.AppNotificationViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.CourseTrackingViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.TransactionChartViewModel
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
    single<UserDatabase> {
        UserDatabase(
            databaseDriverFactory = get(),
        )
    }
    single<Settings> { Settings() }
    single<FirebaseAuth> {
        Firebase.auth // Asegúrate de que FirebaseApp esté inicializado antes de acceder a esto
    }
    single<FirebaseFirestore> {
        Firebase.firestore
    }
    single<CategoryDatabase> { CategoryDatabase(databaseDriverFactory = get()) }
    single<TransactionDatabase> { TransactionDatabase(databaseDriverFactory = get()) }
    single {
        CategoryRepository(
            firestore = get<FirebaseFirestore>(),
            categoryDatabase = get<CategoryDatabase>()
        )
    }
    single {
        TransactionRepository(
            firestore = get<FirebaseFirestore>(),
            transactionDatabase = get<TransactionDatabase>()
        )
    }
    single { FirebaseCourseTrackingDataSource() }
    single {
        LocalCourseTrackingDataSource(
            databaseDriverFactory = get<DatabaseDriverFactory>(),
        )
    }
    single<CourseTrackingRepository> {
        CourseTrackingRepositoryImpl(
            localDataSource = get<LocalCourseTrackingDataSource>(),
            remoteDataSource = get<FirebaseCourseTrackingDataSource>()
        )
    }
    single { FirebaseRewardsDataSource() }
    single {
        LocalRewardsDataSource(
            databaseDriverFactory = get<DatabaseDriverFactory>(),
        )
    }
    single<RewardsRepository> {
        RewardsRepositoryImpl(
            localDataSource = get<LocalRewardsDataSource>(),
            remoteDataSource = get<FirebaseRewardsDataSource>(),
        )
    }

    single<NotificationRepository> {
        NotificationRepositoryImpl(
            remoteDataSource = get<RemoteNotification>(),
            localDataSource = get<LocalNotification>()
        )
    }
    single<RemoteNotification> {
        RemoteNotification()
    }
    single<LocalNotification> {
        LocalNotification(
            databaseDriverFactory = get<DatabaseDriverFactory>(),
        )
    }
}

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            authRepository = AuthRepositoryImpl(
                auth = get<FirebaseAuth>(),
                database = get<FirebaseFirestore>(),
                settings = get<Settings>(),
                userDatabase = get<UserDatabase>(),
            )
        )
    }
    viewModel {
        CourseTrackingViewModel(
            courseTrackingRepository = get(),
            rewardRepository = get(),
        )
    }
    viewModel {
        TransactionChartViewModel(
            transactionRepository = get(),
            categoryRepository = get(),
        )
    }
    viewModel {
        AppNotificationViewModel(
            notificationRepository = get(),
        )
    }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule, viewModelModule)
    }
}
