package app.netlify.dev4rju9.runningtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.netlify.dev4rju9.runningtracker.R
import app.netlify.dev4rju9.runningtracker.db.RunDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var runDao: RunDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}