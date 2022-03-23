package com.krivochkov.homework_2.presentation

interface Item {

    fun areItemsTheSame(otherItem: Item): Boolean

    fun areContentsTheSame(otherItem: Item): Boolean

    fun getType(): Int
}