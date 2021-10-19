package com.slicedwork.trackmysleep.ui.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.slicedwork.trackmysleep.data.source.local.database.SleepDatabase
import com.slicedwork.trackmysleep.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    private lateinit var fragmentSleepQualityBinding: FragmentSleepQualityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentSleepQualityBinding = FragmentSleepQualityBinding.inflate(
            inflater, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val args = SleepQualityFragmentArgs.fromBundle(requireArguments())

        val sleepQualityViewModelFactory =
            SleepQualityViewModelFactory(args.sleepNightKey, dataSource)

        val sleepQualityViewModel =
            ViewModelProvider(this, sleepQualityViewModelFactory)
                .get(SleepQualityViewModel::class.java)

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })

        fragmentSleepQualityBinding.sleepQualityViewModel = sleepQualityViewModel
        fragmentSleepQualityBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentSleepQualityBinding.root
    }
}
