package com.example.project.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project.ui.container.ContainerFragment

class ContainerAdapter(
    fragment: ContainerFragment,
    val tabs: List<Fragment>
): FragmentStateAdapter(fragment) {

    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }
}