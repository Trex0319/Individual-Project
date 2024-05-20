package com.example.project.ui.updateTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project.databinding.FragmentUpdateBinding
import kotlinx.coroutines.launch

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val editViewModel: UpdateViewModel by viewModels { UpdateViewModel.Factory }
    private var selectedItemId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(
            layoutInflater,
            container,
            false
        )
        editViewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            task.let {
                editViewModel.setTask(it)
            }
        }

        //to get the id of the selected task
        arguments?.let {
            selectedItemId = UpdateFragmentArgs.fromBundle(it).id
            editViewModel.getTaskById(selectedItemId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editBook = editViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            editViewModel.finish.collect {
                Log.d("UpdateFragment", "Update finished")
                findNavController().popBackStack()
            }
        }

        editViewModel.snackbar.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}