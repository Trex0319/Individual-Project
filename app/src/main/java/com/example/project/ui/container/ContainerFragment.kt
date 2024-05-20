package com.example.project.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.project.databinding.FragmentContainerBinding
import com.example.project.ui.adapter.ContainerAdapter
import com.example.project.ui.completed.CompletedFragment
import com.example.project.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    private val viewModel: ContainerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpTabs.adapter  = ContainerAdapter(this, listOf(HomeFragment(), CompletedFragment()))

        TabLayoutMediator(binding.tlTabs, binding.vpTabs) { tab, position ->
            when (position) {
                0 -> tab.text = "New Task"
                else -> tab.text = "Completed Task"
            }
        }.attach()

    }

}