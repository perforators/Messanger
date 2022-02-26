package com.krivochkov.homework1

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.provider.CalendarContract.Events
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.krivochkov.homework1.model.CalendarEvent
import com.krivochkov.homework1.util.CUSTOM_CALENDAR_EVENTS_FILTER
import com.krivochkov.homework1.util.CALENDAR_EVENTS_EXTRA
import com.krivochkov.homework1.util.convertLongToTime
import com.krivochkov.homework1.util.getDefaultEventTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CalendarEventsService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        launch(coroutineContext) {
            val projection = arrayOf(
                Events._ID,
                Events.TITLE,
                Events.DTSTART,
                Events.DTEND
            )
            val cursor = contentResolver.query(
                Events.CONTENT_URI,
                projection,
                null,
                null,
                Events._ID + " DESC"
            )
            val calendarEvents = arrayListOf<CalendarEvent>()

            if (cursor != null) {
                with(cursor) {
                    while (moveToNext()) {
                        val id = getString(0)
                        val title = getString(1)
                        val startDate = getLong(2)
                        val endDate = getLong(3)
                        val calendarEvent = CalendarEvent(
                            id,
                            if (title.isEmpty()) resources.getDefaultEventTitle(id) else title,
                            startDate.convertLongToTime(),
                            endDate.convertLongToTime()
                        )
                        calendarEvents.add(calendarEvent)
                    }
                }
                cursor.close()
            }

            sendBroadcast(calendarEvents)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun sendBroadcast(calendarEvents: ArrayList<CalendarEvent>) {
        val intent = Intent(CUSTOM_CALENDAR_EVENTS_FILTER)
            .putParcelableArrayListExtra(CALENDAR_EVENTS_EXTRA, calendarEvents)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.sendBroadcast(intent)
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, CalendarEventsService::class.java)
    }
}