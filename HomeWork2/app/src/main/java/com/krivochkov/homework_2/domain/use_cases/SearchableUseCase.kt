package com.krivochkov.homework_2.domain.use_cases

import io.reactivex.Single

interface SearchableUseCase<T> {

    operator fun invoke(filter: ((T) -> Boolean)? = null): Single<List<T>>
}