package com.krivochkov.homework_2.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserProfileUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val loadMyUserProfileUseCase: LoadMyUserProfileUseCase = LoadMyUserProfileUseCase()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    init {
        loadMyProfile()
    }

    fun loadMyProfile() {
        loadMyUserProfileUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _state.value = ScreenState.Loading }
            .subscribeBy(
                onSuccess = {
                    _state.value = ScreenState.ProfileLoaded(it)
                },
                onError = {
                    _state.value = ScreenState.Error
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}