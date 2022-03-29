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
            is ScreenState.ProfileLoaded -> showContent(state.user)
            is ScreenState.Loading -> showLoading()
            is ScreenState.Error -> showError()
        }
    }

    private fun showContent(user: User) {
        hideError()
        hideLoading()
        binding.profile.apply {
            avatar.setImageResource(R.mipmap.ic_launcher_round)
            fullName.text = user.fullName
            onlineStatus.text = getString(R.string.online_status)
            onlineStatus.setTextColor(requireContext().getColor(R.color.online_status))
            profileLayout.isVisible = true
        }
    }

    private fun showLoading() {
        hideContent()
        hideError()
        binding.loading.loadingLayout.isVisible = true
        binding.loading.loadingLayout.startShimmer()
    }

    private fun showError() {
        hideContent()
        hideLoading()
        binding.error.isVisible = true
    }

    private fun hideContent() {
        binding.profile.profileLayout.isVisible = false
    }

    private fun hideLoading() {
        binding.loading.loadingLayout.isVisible = false
        binding.loading.loadingLayout.stopShimmer()
    }

    private fun hideError() {
        binding.error.isVisible = false
    }
}