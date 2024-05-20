package com.example.project.ui.addBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project.core.Constants
import com.example.project.databinding.FragmentAddBinding
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val viewModel: AddViewModel by viewModels { AddViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel //Connect ViewModel to layout.
        binding.lifecycleOwner = viewLifecycleOwner //Use fragment's lifecycle for LiveData.

        //Coroutine: Tasks that can pause and resume asynchronously.
        //lifecycleScope ensures that the coroutine is automatically cancelled when the fragment is destroyed.
        lifecycleScope.launch {
            //listen for changes in the ViewModel's state or events.
            viewModel.finish.collect {
                //basically to pass the need to refresh to the result below
                val bundle = Bundle().apply {
                    putBoolean(Constants.REFRESH, true)
                }
                //set result to communicate to the previous fragment
                setFragmentResult(Constants.ADD_TASK_FRAGMENT, bundle)
                //to return to the previous page
                findNavController().popBackStack()
            }
        }
    }
}