package com.krivochkov.homework_2.presentation.profile

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentProfileBinding
import com.krivochkov.homework_2.domain.models.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

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

        viewModel.state.observe(this) { state ->
            render(state)
        }
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            viewModel.loadMyProfile()
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.ProfileLoaded -> {
                changeLoadingVisibility(false)
                changeErrorVisibility(false)
                changeContentVisibility(true)
                showProfile(state.user)
            }
            is ScreenState.Loading -> {
                changeContentVisibility(false)
                changeErrorVisibility(false)
                changeLoadingVisibility(true)
            }
            is ScreenState.Error -> {
                changeLoadingVisibility(false)
                changeContentVisibility(false)
                changeErrorVisibility(true)
            }
        }
    }

    private fun showProfile(user: User) {
        binding.profile.apply {
            avatar.setImageResource(R.mipmap.ic_launcher_round)
            fullName.text = user.fullName
            onlineStatus.text = getString(R.string.online_status)
            onlineStatus.setTextColor(requireContext().getColor(R.color.online_status))
            profileLayout.isVisible = true
        }
    }

    private fun changeLoadingVisibility(visibility: Boolean) {
        binding.loading.loadingLayout.apply {
            isVisible = visibility
            if (visibility) startShimmer() else stopShimmer()
        }
    }

    private fun changeErrorVisibility(visibility: Boolean) {
        binding.error.isVisible = visibility
    }

    private fun changeContentVisibility(visibility: Boolean) {
        binding.profile.profileLayout.isVisible = visibility
    }
}