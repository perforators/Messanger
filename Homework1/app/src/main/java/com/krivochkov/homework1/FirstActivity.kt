package com.krivochkov.homework1

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.krivochkov.homework1.databinding.ActivityFirstBinding
import android.Manifest
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.krivochkov.homework1.model.CalendarEvent
import com.krivochkov.homework1.util.CALENDAR_EVENTS_EXTRA
import com.krivochkov.homework1.util.CALENDAR_EVENTS_KEY

class FirstActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityFirstBinding
    private lateinit var calendarEventsAdapter: CalendarEventsAdapter

    private val secondActivityLauncher
        = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    val intent = SecondActivity.createIntent(this)
                    startActivityForResult.launch(intent)
                }
                else -> {
                    showToast(getString(R.string.no_permission))
                }
            }
        }

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val calendarEvents = result.data?.getParcelableArrayListExtra<CalendarEvent>(
                    CALENDAR_EVENTS_EXTRA
                ) ?: listOf()
                updateCalendarEvents(calendarEvents)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initToolbar()
        initRecycler()

        viewBinding.getCalendarEventsButton.setOnClickListener {
            secondActivityLauncher.launch(Manifest.permission.READ_CALENDAR)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val calendarEvents = ArrayList(calendarEventsAdapter.currentList)
        outState.putParcelableArrayList(CALENDAR_EVENTS_KEY, calendarEvents)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val calendarEvents = savedInstanceState.getParcelableArrayList<CalendarEvent>(
            CALENDAR_EVENTS_KEY
        ) ?: listOf()
        updateCalendarEvents(calendarEvents)
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_first_activity_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initRecycler() {
        calendarEventsAdapter = CalendarEventsAdapter()
        viewBinding.recyclerView.apply {
            adapter = calendarEventsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun updateCalendarEvents(calendarEvents: List<CalendarEvent>) {
        calendarEventsAdapter.submitList(calendarEvents)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}