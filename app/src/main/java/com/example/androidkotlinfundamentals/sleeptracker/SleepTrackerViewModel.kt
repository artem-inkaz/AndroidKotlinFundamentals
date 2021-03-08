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

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.androidkotlinfundamentals.database.SleepDatabaseDao
import com.example.androidkotlinfundamentals.database.SleepNight
import com.example.androidkotlinfundamentals.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        dataSource: SleepDatabaseDao,
        application: Application) : ViewModel() {
//    application: Application) : AndroidViewModel(application)

    // create a LiveData that changes when you want the app to navigate to the SleepQualityFragment.
    // Use encapsulation to only expose a gettable version of the LiveData to the ViewModel.
    /**
     * Variable that tells the Fragment to navigate to a specific [SleepQualityFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    /**
     * If this is non-null, immediately navigate to [SleepQualityFragment] and call [doneNavigating]
     */
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    /**
     * Hold a reference to SleepDatabase via SleepDatabaseDao.
     */
    val database = dataSource
    val nights = database.getAllNights() // variable, which stores all the sleep nights, which is the data to display

    /**
     * Converted nights to Spanned for displaying.
     */
    // преобразования ночей в nightString. Используйте функцию formatNights () из Util.kt.
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    // Определите переменную с именем tonight, которая будет содержать текущую ночь.
    // Сделайте переменную MutableLiveData, потому что вам нужно иметь возможность
    // наблюдать за данными и изменять их.
    private var tonight = MutableLiveData<SleepNight?>()
    // Чтобы инициализировать переменную tonight как можно скорее, создайте блок
    // инициализации под определением tonight и вызовите initializeTonight ().

    /**
     * If tonight has not been set, then the START button should be visible.
     */
    // The Start button should be enabled when tonight is null.
    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }

    /**
     * If tonight has been set, then the STOP button should be visible.
     */
    // The Stop button should be enabled when tonight is not null.
    val stopButtonVisible = Transformations.map(tonight) {
        it != null
    }

    /**
     * If there are any nights in the database, show the CLEAR button.
     */
    // The Clear button should only be enabled if nights, and thus the database, contains sleep nights.
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackbarEvent: LiveData<Boolean>
    get() = _showSnackbarEvent

    /**
     * Call this immediately after navigating to [SleepQualityFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    // doneNavigating() function that resets the variable that triggers navigation.
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }
    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar(){
        _showSnackbarEvent.value = false
    }


    init {
        initializeTonight()
    }

    // viewModelScope.launch, чтобы запустить сопрограмму в ViewModelScope.
    // Внутри фигурных скобок получите значение для tonight из базы данных,
    // вызвав getTonightFromDatabase (), и присвойте значение tonight.value
    private fun initializeTonight() {
        // Обратите внимание на фигурные скобки для запуска.
        // Они определяют лямбда-выражение, которое представляет собой функцию без имени.
        // В этом примере вы передаете лямбду конструктору запуска сопрограмм.
        // Этот конструктор создает сопрограмму и назначает выполнение
        // этой лямбды соответствующему диспетчеру.
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
    // Implement getTonightFromDatabase()
    // Inside the function body of getTonightFromDatabase(),
    // get tonight (the newest night) from the database.
    // If the start and end times are not the same, meaning
    // that the night has already been completed, return null.
    // Otherwise, return the night.
    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    // implement onStartTracking(), the click handler for the Start button.
    // You need to create a new SleepNight, insert it into the database,
    // and assign it to tonight. The structure of onStartTracking()
    // is going to be similar to initializeTonight()
    fun onStartTracking() {
        viewModelScope.launch {
            // создайте новый SleepNight, который фиксирует текущее время как время начала.
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }

    // If the end time hasn't been set yet, set the endTimeMilli to the current
    // system time and call update() with the night data.
    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            // В обработчике щелчка для кнопки «Стоп» onStopTracking () активируйте переход
            // к SleepQualityFragment. Установите переменную _navigateToSleepQuality
            // в конце функции как последнюю вещь внутри блока launch {}.
            // Обратите внимание, что эта переменная установлена на ночь.
            // Когда эта переменная имеет значение, приложение переходит к SleepQualityFragment,
            // проходя по ночам.
            _navigateToSleepQuality.value = oldNight
        }
    }

    // Implement update() using the same pattern as you used to implement insert().
    // and android:onClick="@{() -> sleepTrackerViewModel.onStopTracking()}"
    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStart() {
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = SleepNight()

            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }

    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()
            // And clear tonight since it's no longer in the database
            tonight.value = null
            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStop() {
        viewModelScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch().
            val oldNight = tonight.value ?: return@launch

            // Update the night in the database to add the end time.
            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)

            // Set state to navigate to the SleepQualityFragment.
            _navigateToSleepQuality.value = oldNight
        }
    }

    suspend fun clear() {
        database.clear()
    }
}

