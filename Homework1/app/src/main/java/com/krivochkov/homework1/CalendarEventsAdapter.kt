package com.krivochkov.homework1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework1.databinding.CalendarEventItemBinding
import com.krivochkov.homework1.model.CalendarEvent

class CalendarEventsAdapter
    : ListAdapter<CalendarEvent, CalendarEventsAdapter.CalendarEventsViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<CalendarEvent>() {

        override fun areItemsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent) =
            oldItem == newItem
    }

    class CalendarEventsViewHolder(private val binding: CalendarEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(calendarEvent: CalendarEvent) {
            binding.apply {
                title.text = calendarEvent.title
                date.text = String.format(
                    context.getString(R.string.event_date),
                    calendarEvent.startDate,
                    calendarEvent.endDate
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventsViewHolder {
        return CalendarEventsViewHolder(
            CalendarEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holderCalendarEvents: CalendarEventsViewHolder, position: Int) {
        val calendarEvent = getItem(position)
        holderCalendarEvents.bind(calendarEvent)
    }
}