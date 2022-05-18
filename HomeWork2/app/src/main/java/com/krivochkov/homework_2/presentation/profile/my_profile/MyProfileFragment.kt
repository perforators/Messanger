package com.krivochkov.homework_2.presentation.profile.my_profile

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentMyProfileBinding
import com.krivochkov.homework_2.di.profile.DaggerMyProfileScreenComponent
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.MyProfileEffect
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.MyProfileEvent
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.MyProfileState
import com.krivochkov.homework_2.presentation.profile.utils.getColorByStatus
import com.krivochkov.homework_2.utils.loadImage
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

class MyProfileFragment : ElmFragment<MyProfileEvent, MyProfileEffect, MyProfileState>(R.layout.fragment_my_profile) {

    @Inject
    internal lateinit var profileViewModelFactory: MyProfileViewModelFactory

    private val binding: FragmentMyProfileBinding by viewBinding()

    private val viewModel: MyProfileViewModel by viewModels { profileViewModelFactory }

    override val initEvent: MyProfileEvent
        get() = MyProfileEvent.Ui.Init

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMyProfileScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun createStore() = viewModel.peopleStore

    override fun render(state: MyProfileState) {
        binding.apply {
            loading.loadingLayout.apply {
                isVisible = state.isLoading
                if (state.isLoading) startShimmer() else stopShimmer()
            }

            profile.profileLayout.isVisible = state.isLoading.not() && state.error == null
            state.profile?.let { showProfile(it) }

            error.isVisible = state.error != null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            store.accept(MyProfileEvent.Ui.LoadMyProfile)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun showProfile(user: User) {
        binding.profile.apply {
            if (user.avatarUrl == null)
                avatar.setImageResource(R.mipmap.ic_launcher)
            else
                avatar.loadImage(user.avatarUrl)
            fullName.text = user.fullName
            onlineStatus.text = user.status
            onlineStatus.setTextColor(getColorByStatus(user.status, requireContext()))
            profileLayout.isVisible = true
        }
    }
}