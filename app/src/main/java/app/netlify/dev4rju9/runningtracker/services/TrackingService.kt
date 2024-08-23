package app.netlify.dev4rju9.runningtracker.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.netlify.dev4rju9.runningtracker.R
import app.netlify.dev4rju9.runningtracker.other.Constants.ACTION_PAUSE_SERVICE
import app.netlify.dev4rju9.runningtracker.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import app.netlify.dev4rju9.runningtracker.other.Constants.ACTION_START_OR_RESUME_SERVICE
import app.netlify.dev4rju9.runningtracker.other.Constants.ACTION_STOP_SERVICE
import app.netlify.dev4rju9.runningtracker.other.Constants.LOCATION_FASTEST_INTERVAL
import app.netlify.dev4rju9.runningtracker.other.Constants.LOCATION_UPDATE_INTERVAL
import app.netlify.dev4rju9.runningtracker.other.Constants.NOTIFICATION_CHANNEL_ID
import app.netlify.dev4rju9.runningtracker.other.Constants.NOTIFICATION_CHANNEL_NAME
import app.netlify.dev4rju9.runningtracker.other.Constants.NOTIFICATION_ID
import app.netlify.dev4rju9.runningtracker.other.TrackingUtility
import app.netlify.dev4rju9.runningtracker.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {

    private var isFirstRun = true
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let {locations ->
                    for (location in locations) {
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    private fun postInitialValues () {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addEmptyPolyline () = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPathPoint (location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service")
                    }
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                            as NotificationManager

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        createNotificationChannel(notificationManager)

                    val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                        .setContentTitle("Running Tracker")
                        .setContentText("00:00:00")
                        .setContentIntent(getMainActivityPendingIntent())

                    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused Service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped Service")
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking (isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = LOCATION_FASTEST_INTERVAL
                    priority = PRIORITY_BALANCED_POWER_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun startForegroundService () {
        addEmptyPolyline()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running Tracker")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIFICATION_ID, notificationBuilder.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun getMainActivityPendingIntent () = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> FLAG_MUTABLE
            else -> FLAG_UPDATE_CURRENT
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel (notificatoinManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificatoinManager.createNotificationChannel(channel)
    }

}