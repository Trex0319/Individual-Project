package com.example.project.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.core.Constants
import com.example.project.data.model.Task
import com.example.project.databinding.FragmentHomeBinding
import com.example.project.databinding.SortViewBinding
import com.example.project.ui.adapter.TodoAdapter
import com.example.project.ui.container.ContainerFragmentDirections
import com.example.project.ui.container.ContainerViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: TodoAdapter
    private lateinit var originalTask: List<Task>
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private val parentViewModel: ContainerViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var selectedSortBy = "Title"
    private var selectedSortOrder = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllTask().collect{ task ->
                originalTask = task
                adapter.setTasks(task)
            }
        }

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect {
                viewModel.getAllTask()
            }
        }

        binding.svSearchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks(newText)
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
                val sortedList = originalTask.sortedWith(compareBy<Task> {
                    if (selectedSortBy == "date") it.date else it.title
                }.run {
                    if (selectedSortOrder == "asc") this else reversed()
                })

                adapter.setTasks(sortedList)
                dialog.dismiss() // Dismiss the dialog
            }

            dialog.show() // Show the dialog
        }

        binding.fabAddNew.setOnClickListener {
            findNavController().navigate(
                ContainerFragmentDirections.actionContainerFragmentToAddFragment()
            )
        }
        setFragmentResultListener(Constants.ADD_TASK_FRAGMENT) { _, result ->
            val refresh = result.getBoolean(Constants.REFRESH, true)
            if (refresh){
                viewModel.getAllTask()
            }
        }
    }

    //to setup the recycler adapter
    private fun setupAdapter() {
        adapter = TodoAdapter(emptyList())

        adapter.listener = object : TodoAdapter.Listener{
            override fun onClick(task: Task) {
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerFragmentToItemFragment(task.id!!)
                )
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = layoutManager
    }

    private fun filterTasks(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.setTasks(originalTask) // Show all words if the query is null or blank
        } else {
            val filteredWords = originalTask.filter {
                it.title.contains(query, ignoreCase = true)
            }
            adapter.setTasks(filteredWords)
        }
    }
}