package app.netlify.dev4rju9.runningtracker.repositories

import app.netlify.dev4rju9.runningtracker.db.Run
import app.netlify.dev4rju9.runningtracker.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDAO
) {

    suspend fun insertRun (run: Run) = runDao.insertRun(run)

    suspend fun deleteRun (run: Run) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate () = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance () = runDao.getAllRunsSortedByDistance()

    fun getAllRunsSortedByTimeInMillis () = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByCaloriesBurned () = runDao.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByAvgSpeed () = runDao.getAllRunsSortedByAvgSpeeed()

    fun getTotalAvgSpeed () = runDao.getTotalAvgSpeed()

    fun getTotalDistance () = runDao.getTotalDistnace()

    fun getTotalCaloriesBurned () = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis () = runDao.getTotalTimeInMillis()

}