package app.netlify.dev4rju9.runningtracker.di

import android.content.Context
import androidx.room.Room
import app.netlify.dev4rju9.runningtracker.db.RunningDatabase
import app.netlify.dev4rju9.runningtracker.other.Constants
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
        Constants.RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun getRunDao (db: RunningDatabase) = db.getRunDao()

}