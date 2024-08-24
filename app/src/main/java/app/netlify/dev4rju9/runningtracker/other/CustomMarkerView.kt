package app.netlify.dev4rju9.runningtracker.other

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import app.netlify.dev4rju9.runningtracker.databinding.MarkerViewBinding
import app.netlify.dev4rju9.runningtracker.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomMarkerView (
    var runs: List<Run>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) return
        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]
        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }

        val binding: MarkerViewBinding = MarkerViewBinding.bind(this)

        val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormatter.format(calendar.time)

        val avgSpeed = "${run.avgSpeeInKMH}km/h"
        binding.tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceInMeter / 1000f}km"
        binding.tvDistance.text = distanceInKm
        binding.tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        binding.tvCaloriesBurned.text = caloriesBurned
    }

}