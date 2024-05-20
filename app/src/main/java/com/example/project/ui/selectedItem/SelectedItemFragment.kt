package com.example.project.ui.selectedItem

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project.databinding.AlertCompletedViewBinding
import com.example.project.databinding.AlertDeleteNoteViewBinding
import com.example.project.databinding.FragmentItemBinding
import kotlinx.coroutines.launch

class SelectedItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private val selectedViewModel: SelectedItemViewModel by viewModels { SelectedItemViewModel.Factory }
    private var selectedItemId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(
            layoutInflater,
            container,
            false
        )

        //to get the note id from the selected note
        arguments?.let {
            selectedItemId = SelectedItemFragmentArgs.fromBundle(it).id
            selectedViewModel.getTask(selectedItemId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedWord()
        setupButtons()
    }

    private fun observeSelectedWord() {
        lifecycleScope.launch {
            selectedViewModel.selectedTask.observe(viewLifecycleOwner) { task ->
                task?.let {
                    binding.run {
                        tvTitle.text = task.title
                        tvMean.text = task.mean
                        tvSyn.text = task.syn
                        tvDetails.text = task.detail
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        binding.mbUpdate.setOnClickListener {
            findNavController().navigate(
                SelectedItemFragmentDirections.actionItemFragmentToUpdateFragment(selectedItemId)
            )
        }

        binding.mbDelete.setOnClickListener {

            val alertView = AlertDeleteNoteViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            alertView.tvTitle.text = "Are you sure?"
            alertView.tvBody.text = "You want to delete this task? \n Action can not be undone."

            val temporaryDeleteDialog = deleteDialog.create()

            alertView.btnDelete.setOnClickListener {
                selectedViewModel.delete()
                findNavController().navigate(
                    SelectedItemFragmentDirections.actionItemFragmentToContainerFragment()
                )
                Toast.makeText(
                    requireContext(),
                    "Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnCancel.setOnClickListener {
                temporaryDeleteDialog.dismiss() // Dismiss the dialog when cancel button is clicked
            }
            // Show the delete dialog
            temporaryDeleteDialog.show()
        }

        binding.mbDone.setOnClickListener {
            selectedViewModel.selectedTask.value?.let { task ->
                val alertView = AlertCompletedViewBinding.inflate(layoutInflater)
                val deleteDialog = AlertDialog.Builder(requireContext())
                deleteDialog.setView(alertView.root)
                alertView.tvTitle.text = "Are you sure?"
                if (task.status == true) {
                    alertView.tvBody.text = "You want to move back this task to new task?"
                } else {
                    alertView.tvBody.text = "You want to move this task to completed?"
                }

                val temporaryDeleteDialog = deleteDialog.create()

                alertView.btnYes.setOnClickListener {
                    selectedViewModel.moveTaskToCompleted()
                    findNavController().navigate(SelectedItemFragmentDirections.actionItemFragmentToContainerFragment())
                    temporaryDeleteDialog.dismiss()
                }

                alertView.btnNo.setOnClickListener {
                    temporaryDeleteDialog.dismiss() // Dismiss the dialog when cancel button is clicked
                }
                temporaryDeleteDialog.show()
            }
        }
    }
}