package com.elamparithi.parkypark.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elamparithi.parkypark.BuildConfig
import com.elamparithi.parkypark.data.local.database.DatabaseService
import com.elamparithi.parkypark.data.local.database.PrePopulateDatabase
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.remote.Networking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getApplicationContext(@ApplicationContext application: Context) = application

    @Provides
    @Singleton
    fun getSharedPreference(@ApplicationContext application: Context): SharedPreferences =
        application.getSharedPreferences("parky_user_prefs", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext application: Context,
        prePopulateDatabase: PrePopulateDatabase
    ): DatabaseService {
        Timber.e("hilt database creation")
        return Room.databaseBuilder(
            application,
            DatabaseService::class.java,
            "parky_park_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.e("oncreate")
                    prePopulateDatabase.insertParkingData()
                }
            }).build()
    }

    @Singleton
    @Provides
    fun provideNetworkService() = Networking.create(
        BuildConfig.BASE_URL,
        BuildConfig.API_KEY
    )

    @Singleton
    @Provides
    fun provideBookingDao(database: DatabaseService) = database.bookingDao()

    @Singleton
    @Provides
    fun providePaymentDao(database: DatabaseService) = database.paymentDao()

    @Singleton
    @Provides
    fun provideCouponDao(database: DatabaseService) = database.couponDao()

    @Singleton
    @Provides
    fun provideCouponUserRelDao(database: DatabaseService) = database.couponUserRelDao()

    @Singleton
    @Provides
    fun provideParkingFloorDao(database: DatabaseService) = database.parkingFloorDao()

    @Singleton
    @Provides
    fun provideParkingLotDao(database: DatabaseService) = database.parkingLotDao()

    @Singleton
    @Provides
    fun provideParkingSpaceDao(database: DatabaseService) = database.parkingSpaceDao()

    @Singleton
    @Provides
    fun provideUserDao(database: DatabaseService) = database.userDao()

    @Singleton
    @Provides
    fun provideVehicleDao(database: DatabaseService) = database.vehicleDao()

    @Singleton
    @Provides
    fun provideVehicleTypeDao(database: DatabaseService) = database.vehicleTypeDao()

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Singleton
    @Provides
    fun providePrePopulateDatabase(
        parkingLotProvider: Provider<ParkingLotDao>,
        parkingFloorProvider: Provider<ParkingFloorDao>,
        parkingSpaceProvider: Provider<ParkingSpaceDao>,
        vehicleTypeProvider: Provider<VehicleTypeDao>,
        couponProvider: Provider<CouponDao>,
        coroutineScope: CoroutineScope
    ) = PrePopulateDatabase(
        parkingLotProvider,
        parkingFloorProvider,
        parkingSpaceProvider,
        vehicleTypeProvider,
        couponProvider,
        coroutineScope
    )

}