package com.slicedwork.trackmysleep.ui.sleeptracker

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.slicedwork.trackmysleep.R
import com.slicedwork.trackmysleep.data.source.local.dao.SleepDatabaseDao
import com.slicedwork.trackmysleep.data.source.local.database.SleepDatabase
import com.slicedwork.trackmysleep.databinding.FragmentSleepTrackerBinding
import com.slicedwork.trackmysleep.ui.adapter.NightsAdapter
import com.slicedwork.trackmysleep.util.formatNights

class SleepTrackerFragment : Fragment() {

    private lateinit var dataSource: SleepDatabaseDao
    private lateinit var fragmentSleepTrackerBinding: FragmentSleepTrackerBinding
    private lateinit var application: Application
    private lateinit var sleepTrackerViewModelFactory: SleepTrackerViewModelFactory
    private lateinit var sleepTrackerViewModel: SleepTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentSleepTrackerBinding =
            FragmentSleepTrackerBinding.inflate(inflater, container, false)

        application = requireNotNull(this.activity).application
        dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        sleepTrackerViewModelFactory = SleepTrackerViewModelFactory(dataSource)
        sleepTrackerViewModel = ViewModelProvider(
            this,
            sleepTrackerViewModelFactory
        ).get(SleepTrackerViewModel::class.java)

        fragmentSleepTrackerBinding.sleepTrackerViewModel = sleepTrackerViewModel
        fragmentSleepTrackerBinding.lifecycleOwner = this

        sleepTrackerViewModel.sleepNightsLiveData.observe(viewLifecycleOwner, { sleepNights ->
            val nights = formatNights(sleepNights, application.resources)
            fragmentSleepTrackerBinding.rvSleepList.apply {
                adapter = NightsAdapter(nights)
                layoutManager = LinearLayoutManager(this@SleepTrackerFragment.context)
            }
        })

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(
                        night.nightId
                    )
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        return fragmentSleepTrackerBinding.root
    }
}
