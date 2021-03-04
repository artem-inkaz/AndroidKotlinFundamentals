package com.example.androidkotlinfundamentals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // Contains all the views
//   private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//     val binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
    }

    override fun onStart() {
        super.onStart()
 //       Log.i("MainActivity","onStart Called")
        Timber.i("onCreate called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }
    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }
    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }
    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }
}