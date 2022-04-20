package com.example.kalkulator

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.lang.Math.round

class SettingsActivity2: AppCompatActivity() {

    var darkMode:Boolean = false
    var eps: Int = 2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)
        val darkModeSwitch: Switch = findViewById(R.id.darkModeSwitch)
        val precissionBar: SeekBar = findViewById(R.id.precisionBar)
        val precission: TextView = findViewById(R.id.precision)
        val extras = intent.extras?.deepCopy() ?: return
        val d = AppCompatDelegate.getDefaultNightMode()
        if(d == AppCompatDelegate.MODE_NIGHT_YES){
            darkMode = true
            darkModeSwitch.isChecked = true
        }
        else{
            darkMode = false
            darkModeSwitch.isChecked = false
        }
        eps = extras.getInt("Eps")
        precissionBar.progress = eps
        precission.text = "Precission: " + precissionBar.progress.toString()
        precissionBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                precission.text = "Precission: " + precissionBar.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}

            override fun onStopTrackingTouch(seek: SeekBar) {
                eps = precissionBar.progress
            }
        })
        }

    fun switchFun(v: View){
        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun finish() {
        val i = Intent()
        val b = Bundle()
        b.putBoolean("DarkMode", darkMode)
        b.putInt("Eps", eps)

        i.putExtras(b)
        setResult(Activity.RESULT_OK,i)

        super.finish()
    }
}