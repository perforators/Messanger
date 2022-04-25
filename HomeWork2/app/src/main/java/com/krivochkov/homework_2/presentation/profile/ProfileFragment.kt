package com.krivochkov.homework_2.presentation.profile

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentProfileBinding
import com.krivochkov.homework_2.di.GlobalDI
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.presentation.profile.elm.ProfileEffect
import com.krivochkov.homework_2.presentation.profile.elm.ProfileEvent
import com.krivochkov.homework_2.presentation.profile.elm.ProfileState
import com.krivochkov.homework_2.utils.loadImage
import vivid.money.elmslie.android.base.ElmFragment

class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override val initEvent: ProfileEvent
        get() = ProfileEvent.Ui.Init

    override fun createStore() =
        GlobalDI.INSTANCE.presentationModule.profileStoreFactory.provide()

    override fun render(state: ProfileState) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.bind(
            inflater.inflate(R.layout.fragment_profile, container, false)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            store.accept(ProfileEvent.Ui.LoadMyProfile)
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
            onlineStatus.setTextColor(getColorByStatus(user.status))
            profileLayout.isVisible = true
        }
    }

    private fun getColorByStatus(status: String): Int {
        return when (status) {
            ACTIVE_STATUS -> requireContext().getColor(R.color.online_status)
            IDLE_STATUS -> requireContext().getColor(R.color.idle_status)
            OFFLINE_STATUS -> requireContext().getColor(R.color.offline_status)
            else -> requireContext().getColor(R.color.offline_status)
        }
    }

    companion object {
        const val OFFLINE_STATUS = "offline"
        const val ACTIVE_STATUS = "active"
        const val IDLE_STATUS = "idle"
    }
}