package com.krivochkov.homework_2.data.sources.remote.request

class Request(val queryMap: Map<String, String>) {

    class Builder {

        private val queryMap: MutableMap<String, String> = mutableMapOf()

        fun addQuery(queryKey: String, param: String) = apply { queryMap[queryKey] = param }

        fun build() = Request(queryMap)
    }
}