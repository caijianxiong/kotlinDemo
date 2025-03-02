package com.kandaovr.meeting.kotlinDemo.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kandaovr.meeting.kotlinDemo.R
import kotlinx.coroutines.flow.map


// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TestActivity : AppCompatActivity() {
    private val KEY_AGE = intPreferencesKey("key_age")
    private val KEY_NAME = stringPreferencesKey("key_name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }


    fun setPreferenceValue() {
        dataStore.data.map { pre ->
            pre[KEY_AGE] ?: 18
        }
    }


}