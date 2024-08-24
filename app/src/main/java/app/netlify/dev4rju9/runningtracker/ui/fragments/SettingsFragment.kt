package app.netlify.dev4rju9.runningtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.netlify.dev4rju9.runningtracker.R
import app.netlify.dev4rju9.runningtracker.databinding.FragmentSettingsBinding
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_NAME
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_WEIGHT
import app.netlify.dev4rju9.runningtracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    @Inject
    lateinit var sharedPref: SharedPreferences
    @Inject
    lateinit var name : String
    @set:Inject
    var weight = 50f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        loadFieldsFromSharedPref()

        binding.btnApplyChanges.setOnClickListener {
            if (applyChangesToSharedPreferences()) {
                Snackbar.make(
                    view,
                    "Saved changes",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    view,
                    "Please fill out all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun loadFieldsFromSharedPref () {
        binding.etName.setText(name)
        binding.etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPreferences () : Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit()
            .putString(KEY_USER_NAME, name)
            .putFloat(KEY_USER_WEIGHT, weight.toFloat())
            .apply()
        val toolbarText = "Let's go $name"
        (requireActivity() as MainActivity).binding.tvToolbarTitle.text = toolbarText
        return true
    }

}