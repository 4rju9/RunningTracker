package app.netlify.dev4rju9.runningtracker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import app.netlify.dev4rju9.runningtracker.db.RunningDatabase
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_NAME
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_WEIGHT
import app.netlify.dev4rju9.runningtracker.other.Constants.RUNNING_DATABASE_NAME
import app.netlify.dev4rju9.runningtracker.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase (
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun getRunDao (db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences (@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName (sharedPref: SharedPreferences) =
        sharedPref.getString(KEY_USER_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight (sharedPref: SharedPreferences) =
        sharedPref.getFloat(KEY_USER_WEIGHT, 50f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle (sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}