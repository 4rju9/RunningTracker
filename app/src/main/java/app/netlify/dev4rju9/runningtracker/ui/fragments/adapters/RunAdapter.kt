package app.netlify.dev4rju9.runningtracker.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.netlify.dev4rju9.runningtracker.databinding.ItemRunBinding
import app.netlify.dev4rju9.runningtracker.db.Run
import app.netlify.dev4rju9.runningtracker.other.TrackingUtility
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder (val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Run, newItem: Run) =
            oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList (list: List<Run>) = differ.submitList(list)
    fun getList () = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = getList().size

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = getList()[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(run.img).into(ivRunImage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            tvDate.text = dateFormatter.format(calendar.time)
            val avgSpeed = "${run.avgSpeeInKMH}km/h"
            tvAvgSpeed.text = avgSpeed
            val distanceInKm = "${run.distanceInMeter / 1000f}km"
            tvDistance.text = distanceInKm
            tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
            val caloriesBurned = "${run.caloriesBurned}kcal"
            tvCalories.text = caloriesBurned
        }
    }

}