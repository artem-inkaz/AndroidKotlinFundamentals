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
import androidx.lifecycle.*
import com.example.androidkotlinfundamentals.database.SleepDatabaseDao
import com.example.androidkotlinfundamentals.database.SleepNight
import com.example.androidkotlinfundamentals.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        // create a LiveData that changes when you want the app to navigate to the SleepQualityFragment.
        // Use encapsulation to only expose a gettable version of the LiveData to the ViewModel.
        private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
        val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality


      private val nights = database.getAllNights()
        // преобразования ночей в nightString. Используйте функцию formatNights () из Util.kt.
    val nightsString = Transformations.map(nights){nights ->
            formatNights(nights,application.resources)
        }
        // Определите переменную с именем tonight, которая будет содержать текущую ночь.
        // Сделайте переменную MutableLiveData, потому что вам нужно иметь возможность
        // наблюдать за данными и изменять их.
      private var tonight = MutableLiveData<SleepNight?>()
        // Чтобы инициализировать переменную tonight как можно скорее, создайте блок
        // инициализации под определением tonight и вызовите initializeTonight ().
        init {
            initializeTonight()
        }
        // viewModelScope.launch, чтобы запустить сопрограмму в ViewModelScope.
        // Внутри фигурных скобок получите значение для tonight из базы данных,
        // вызвав getTonightFromDatabase (), и присвойте значение tonight.value
        private fun initializeTonight(){
                // Обратите внимание на фигурные скобки для запуска.
                // Они определяют лямбда-выражение, которое представляет собой функцию без имени.
                // В этом примере вы передаете лямбду конструктору запуска сопрограмм.
                // Этот конструктор создает сопрограмму и назначает выполнение
                // этой лямбды соответствующему диспетчеру.
                viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }
        // Implement getTonightFromDatabase()
        // Inside the function body of getTonightFromDatabase(),
        // get tonight (the newest night) from the database.
        // If the start and end times are not the same, meaning
        // that the night has already been completed, return null.
        // Otherwise, return the night.
        private suspend fun getTonightFromDatabase(): SleepNight? {
                var night = database.getTonight()
                if (night?.endTimeMilli != night?.startTimeMilli){
                        night = null
                }
                return night
        }
        // implement onStartTracking(), the click handler for the Start button.
        // You need to create a new SleepNight, insert it into the database,
        // and assign it to tonight. The structure of onStartTracking()
        // is going to be similar to initializeTonight()
        fun onStartTracking(){
                viewModelScope.launch {
                        // создайте новый SleepNight, который фиксирует текущее время как время начала.
                        val newNight = SleepNight()
                        insert(newNight)
                        tonight.value = getTonightFromDatabase()
                }
        }

        private suspend fun insert(night: SleepNight){
                database.insert(night)
        }

        // If the end time hasn't been set yet, set the endTimeMilli to the current
        // system time and call update() with the night data.
        fun onStopTracking(){
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
    private suspend fun update(night: SleepNight){
        database.update(night)
    }

    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
    }

    suspend fun clear(){
        database.clear()
    }
    // doneNavigating() function that resets the variable that triggers navigation.
    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }

}

