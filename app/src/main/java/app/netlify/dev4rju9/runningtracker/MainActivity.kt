package app.netlify.dev4rju9.runningtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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