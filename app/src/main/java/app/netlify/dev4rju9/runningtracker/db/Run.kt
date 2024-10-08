package app.netlify.dev4rju9.runningtracker.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run (
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeeInKMH: Float = 0f,
    var distanceInMeter: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}