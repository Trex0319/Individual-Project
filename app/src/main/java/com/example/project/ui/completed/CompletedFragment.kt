package com.example.project.ui.completed

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.core.Constants
import com.example.project.data.model.Task
import com.example.project.databinding.FragmentCompletedBinding
import com.example.project.databinding.SortViewBinding
import com.example.project.ui.adapter.CompletedAdapter
import com.example.project.ui.container.ContainerFragmentDirections
import com.example.project.ui.container.ContainerViewModel
import kotlinx.coroutines.launch

class CompletedFragment : Fragment() {
    private lateinit var binding: FragmentCompletedBinding
    private lateinit var adapter: CompletedAdapter
    private lateinit var original: List<Task>
    private val parentViewModel: ContainerViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel: CompletedViewModel by viewModels { CompletedViewModel.Factory }
    private var selectedSortBy = "title"
    private var selectedSortOrder = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllTasks().collect { tasks ->
                // Store the original list of tasks and set the adapter
                original = tasks
                adapter.setTasks(tasks)
            }
        }

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect {
                viewModel.getAllTasks()
            }
        }

        // Search functionality
        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWords(newText)
                return true
            }
        })

        // Search functionality
        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWords(newText)
                return true
            }
        })

        binding.ivSort.setOnClickListener {
            val alertView = SortViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            val dialog = deleteDialog.create() // Create the dialog

            alertView.rbTitle.setOnClickListener {
                // Clear selection of the date radio button
                alertView.rbDate.isChecked = false
                selectedSortBy = "title"
            }
            alertView.rbDate.setOnClickListener {
                // Clear selection of the title radio button
                alertView.rbTitle.isChecked = false
                selectedSortBy = "date"
            }
            alertView.rbAscending.setOnClickListener {
                alertView.rbDescending.isChecked = false
                selectedSortOrder = "asc"
            }
            alertView.rbDescending.setOnClickListener {
                alertView.rbAscending.isChecked = false
                selectedSortOrder = "desc"
            }

            alertView.btnDone.setOnClickListener {
                // Call your sorting functionality passing the selected options
                val sortedList = original.sortedWith(compareBy<Task> {
                    if (selectedSortBy == "date") it.date else it.title
                }.run {
                    if (selectedSortOrder == "asc") this else reversed()
                })

                adapter.setTasks(sortedList)
                dialog.dismiss() // Dismiss the dialog
            }

            dialog.show() // Show the dialog
        }

        setFragmentResultListener(Constants.ADD_TASK_FRAGMENT) { _, result ->
            val refresh = result.getBoolean(Constants.REFRESH, true)
            if (refresh){
                viewModel.getAllTasks()
            }
        }

        setFragmentResultListener(Constants.COMPLETED_FRAGMENT) { _, result ->
            val refresh = result.getBoolean(Constants.REFRESH, true)
            if (refresh){
                viewModel.getAllTasks()
            }
        }
    }

    private fun setupAdapter() {
        adapter = CompletedAdapter(emptyList())
        adapter.listener = object : CompletedAdapter.Listener{
            override fun onClick(task: Task) {
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerFragmentToItemFragment(task.id!!)
                )
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompletedWords.adapter = adapter
        binding.rvCompletedWords.layoutManager = layoutManager
    }

    private fun filterWords(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.setTasks(original) // Show all works if the query is null or blank
        } else {
            val filteredWords = original.filter {
                it.title.contains(query, ignoreCase = true)
            }
            adapter.setTasks(filteredWords)
        }
    }


}