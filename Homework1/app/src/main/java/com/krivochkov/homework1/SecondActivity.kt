package com.krivochkov.homework1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.krivochkov.homework1.databinding.ActivitySecondBinding
import com.krivochkov.homework1.model.CalendarEvent

class SecondActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySecondBinding

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
        viewBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initToolbar()

        val intentFilter = IntentFilter(CUSTOM_CALENDAR_EVENTS_FILTER)
        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        localBroadcastReceiver.registerReceiver(calendarEventsBroadcastReceiver, intentFilter)
    }

    private fun initToolbar() {
        viewBinding.toolbarLayout.toolbar.apply {
            title = getString(R.string.title_second_activity_toolbar)
            setSupportActionBar(this)
        }
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

        const val CUSTOM_CALENDAR_EVENTS_FILTER = "calendar_events_filter"
        const val CALENDAR_EVENTS_EXTRA = "calendar_events_extra"

        fun createIntent(context: Context) = Intent(context, SecondActivity::class.java)
    }
}