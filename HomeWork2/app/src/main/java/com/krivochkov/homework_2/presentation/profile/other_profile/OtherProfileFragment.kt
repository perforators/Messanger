package com.krivochkov.homework_2.presentation.profile.other_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentOtherProfileBinding
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.presentation.profile.utils.getColorByStatus
import com.krivochkov.homework_2.utils.loadImage

class OtherProfileFragment : Fragment(R.layout.fragment_other_profile) {

    private val binding: FragmentOtherProfileBinding by viewBinding()

    private val args by navArgs<OtherProfileFragmentArgs>()

    private val user: User
        get() = args.user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        showProfile(user)
    }

    private fun initToolbar() {
        binding.toolbarLayout.apply {
            toolbar.title = getString(R.string.profile_title_toolbar)
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
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