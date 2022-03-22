package com.krivochkov.homework_2.presentation.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentProfileBinding

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

        viewModel.myProfile.observe(this) { profile ->
            binding.avatar.setImageResource(R.mipmap.ic_launcher_round)
            binding.fullName.text = profile.fullName
            binding.onlineStatus.text = getString(R.string.online_status)
            binding.onlineStatus.setTextColor(requireContext().getColor(R.color.online_status))
        }
    }
}