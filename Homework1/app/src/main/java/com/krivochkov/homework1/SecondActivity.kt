package com.krivochkov.homework1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.krivochkov.homework1.model.CalendarEvent
import com.krivochkov.homework1.util.CUSTOM_CALENDAR_EVENTS_FILTER
import com.krivochkov.homework1.util.CALENDAR_EVENTS_EXTRA

class SecondActivity : AppCompatActivity() {

    private val calendarEventsBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val contacts = intent.getParcelableArrayListExtra<CalendarEvent>(
                CALENDAR_EVENTS_EXTRA
            )
            val data = Intent().putParcelableArrayListExtra(CALENDAR_EVENTS_EXTRA, contacts)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        initToolbar()

        val intentFilter = IntentFilter(CUSTOM_CALENDAR_EVENTS_FILTER)
        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        localBroadcastReceiver.registerReceiver(calendarEventsBroadcastReceiver, intentFilter)
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_second_activity_toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()

        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        localBroadcastReceiver.unregisterReceiver(calendarEventsBroadcastReceiver)
    }

    override fun onStart() {
        super.onStart()
        startService(CalendarEventsService.createIntent(this))
    }

    override fun onStop() {
        super.onStop()
        stopService(CalendarEventsService.createIntent(this))
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, SecondActivity::class.java)
    }
}