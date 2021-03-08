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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        // Назначьте переменную привязки sleepTrackerViewModel для sleepTrackerViewModel.
        binding.sleepTrackerViewModel = sleepTrackerViewModel
        //Установите текущее действие как владельца жизненного цикла привязки.
        binding.setLifecycleOwner(this)
        // SleepTrackerFragment должен наблюдать за _navigateToSleepQuality,
        // чтобы приложение знало, когда переходить. В SleepTrackerFragment в onCreateView ()
        // добавьте наблюдателя для navigateToSleepQuality ().
        // Обратите внимание, что импорт для этого неоднозначен,
        // и вам нужно импортировать androidx.lifecycle.Observer.
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer {
            // Внутри блока наблюдателя перейдите и передайте идентификатор текущей ночи,
            // а затем вызовите doneNavigating (). Если ваш импорт неоднозначен,
            // импортируйте androidx.navigation.fragment.findNavController.
                night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                sleepTrackerViewModel.doneNavigating()
            }
        })

        sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            // Inside the observer block, display the snackbar and immediately reset the event.
            if (it == true) {
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }
}
