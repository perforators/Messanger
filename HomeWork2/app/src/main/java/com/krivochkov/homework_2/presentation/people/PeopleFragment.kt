package com.krivochkov.homework_2.presentation.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentPeopleBinding
import com.krivochkov.homework_2.di.people.DaggerPeopleScreenComponent
import com.krivochkov.homework_2.presentation.people.adapter.PeopleAdapter
import com.krivochkov.homework_2.presentation.people.elm.PeopleEffect
import com.krivochkov.homework_2.presentation.people.elm.PeopleEvent
import com.krivochkov.homework_2.presentation.people.elm.PeopleState
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>() {

    @Inject
    internal lateinit var peopleViewModelFactory: PeopleViewModelFactory

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PeopleAdapter

    private val viewModel: PeopleViewModel by viewModels { peopleViewModelFactory }

    override val initEvent: PeopleEvent
        get() = PeopleEvent.Ui.Init

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerPeopleScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun createStore() = viewModel.peopleStore

    override fun render(state: PeopleState) {
        binding.apply {
            peopleRecycler.isVisible = state.isLoading.not() && state.error == null

            adapter.submitUsers(state.people) {
                loading.loadingLayout.apply {
                    isVisible = state.isLoading
                    if (state.isLoading) startShimmer() else stopShimmer()
                }
            }

            error.isVisible = state.error != null
        }
    }

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
        initErrorView()

        viewModel.searchQuery.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {
                store.accept(PeopleEvent.Ui.SearchPeople(it))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initSearchLayout()
    }

    private fun initRecycler() {
        adapter = PeopleAdapter()
        binding.peopleRecycler.adapter = adapter
        binding.peopleRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            store.accept(PeopleEvent.Ui.SearchPeopleByLastQuery)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initSearchLayout() {
        binding.search.searchView.hint =
            requireContext().getString(R.string.hint_search_view_users)

        binding.search.searchView.addTextChangedListener { text ->
            viewModel.addQueryToQueue(text?.toString().orEmpty())
        }
    }
}