package app.netlify.dev4rju9.runningtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import app.netlify.dev4rju9.runningtracker.R
import app.netlify.dev4rju9.runningtracker.databinding.FragmentSetupBinding
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_NAME
import app.netlify.dev4rju9.runningtracker.other.Constants.KEY_USER_WEIGHT
import app.netlify.dev4rju9.runningtracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private lateinit var binding: FragmentSetupBinding
    @Inject
    lateinit var sharedPref: SharedPreferences
    @set:Inject
    var isFirstOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSetupBinding.bind(view)

        if (!isFirstOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )

        }

        binding.tvContinue.setOnClickListener {
            if (writePersonalDataToSharedPref()) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    "Please enter all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun writePersonalDataToSharedPref () : Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit()
            .putString(KEY_USER_NAME, name)
            .putFloat(KEY_USER_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        val toolbarText = "Let's go, $name!"
        (requireActivity() as MainActivity).binding.tvToolbarTitle.text = toolbarText
        return true
    }

}