/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidkotlinfundamentals.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidkotlinfundamentals.R
import com.example.androidkotlinfundamentals.database.SleepDatabase
import com.example.androidkotlinfundamentals.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        // Функция requireNotNull Kotlin выдает исключение IllegalArgumentException,
        // если значение равно нулю.
        val application = requireNotNull(this.activity).application
        //Вам нужна ссылка на ваш источник данных через ссылку на DAO.
        // Чтобы получить ссылку на DAO базы данных, используйте SleepDatabase.getInstance (application) .sleepDatabaseDao.
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        // create an instance of the viewModelFactory
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource,application)
        // get a reference to the SleepTrackerViewModel
        // Get a reference to the ViewModel associated with this fragment.
        val sleepTrackerViewModel =
            ViewModelProvider(
                this,viewModelFactory).get(SleepTrackerViewModel::class.java)
        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        // Назначьте переменную привязки sleepTrackerViewModel для sleepTrackerViewModel.
        binding.sleepTrackerViewModel = sleepTrackerViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData update
        //Установите текущее действие как владельца жизненного цикла привязки.
        binding.setLifecycleOwner(this)
        // SleepTrackerFragment должен наблюдать за _navigateToSleepQuality,
        // чтобы приложение знало, когда переходить. В SleepTrackerFragment в onCreateView ()
        // добавьте наблюдателя для navigateToSleepQuality ().
        // Обратите внимание, что импорт для этого неоднозначен,
        // и вам нужно импортировать androidx.lifecycle.Observer.
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            // We need to get the navController from this, because button is not ready, and it
            // just has to be a view. For some reason, this only matters if we hit stop again
            // after using the back button, not if we hit stop and choose a quality.
            // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
            // popping the stack to get the correct behavior if we press stop multiple times
            // followed by back.
            // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra

            // Внутри блока наблюдателя перейдите и передайте идентификатор текущей ночи,
            // а затем вызовите doneNavigating (). Если ваш импорт неоднозначен,
            // импортируйте androidx.navigation.fragment.findNavController.
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneNavigating()
            }
        })

        // Add an Observer on the state variable for showing a Snackbar message
        // when the CLEAR button is pressed.
        sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            // Inside the observer block, display the snackbar and immediately reset the event.
            if (it == true) { // Observed state is true.
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the toast is only shown once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })
        // create an adapter
        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
        })
        // get a reference to the binding object, associate the adapter with the RecyclerView
        binding.sleepList.adapter = adapter

        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
//        val manager = GridLayoutManager(activity, 5, GridLayoutManager.HORIZONTAL, false)
//        val manager = GridLayoutManager(activity, 1)
        binding.sleepList.layoutManager = manager
        // Предоставляя viewLifecycleOwner фрагмента в качестве владельца жизненного цикла,
        // вы можете убедиться, что этот наблюдатель активен только тогда, когда
        // RecyclerView находится на экране.
        // create an observer on the nights variable
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
           // Inside the observer, whenever you get a non-null value (for nights), assign the value
            // to the adapter's data. This is the completed code for the observer and setting the
            // data:
            it?.let {
//                adapter.data = it
                adapter.submitList(it)

            }
        })
        return binding.root
    }
}
