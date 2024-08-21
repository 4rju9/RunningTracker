package app.netlify.dev4rju9.runningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import app.netlify.dev4rju9.runningtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
}