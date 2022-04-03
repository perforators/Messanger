package com.krivochkov.homework_2.presentation.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentPeopleBinding
import com.krivochkov.homework_2.presentation.people.adapter.PeopleAdapter

class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PeopleAdapter

    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.bind(
            inflater.inflate(R.layout.fragment_people, container, false)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initSearchLayout()
        initErrorView()

        viewModel.state.observe(this) { state ->
            render(state)
        }
    }

    private fun initRecycler() {
        adapter = PeopleAdapter()
        binding.peopleRecycler.adapter = adapter
        binding.peopleRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            viewModel.loadUsersByLastQuery()
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initSearchLayout() {
        binding.search.searchView.hint =
            requireContext().getString(R.string.hint_search_view_users)

        binding.search.searchView.addTextChangedListener { text ->
            viewModel.loadUsers(text?.toString().orEmpty())
        }
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.PeopleLoaded -> {
                changeLoadingVisibility(false)
                changeErrorVisibility(false)
                adapter.submitUsers(state.users) {
                    changeContentVisibility(true)
                }
            }
            is ScreenState.Loading -> {
                changeErrorVisibility(false)
                changeContentVisibility(false)
                changeLoadingVisibility(true)
            }
            is ScreenState.Error -> {
                changeContentVisibility(false)
                changeLoadingVisibility(false)
                changeErrorVisibility(true)
            }
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
        binding.peopleRecycler.isVisible = visibility
    }
}