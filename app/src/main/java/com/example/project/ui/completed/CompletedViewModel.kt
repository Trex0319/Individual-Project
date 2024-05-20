package com.example.project.ui.completed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project.BookApp
import com.example.project.data.model.Task
import com.example.project.data.repository.BookRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CompletedViewModel(private val repo: BookRepo) : ViewModel() {

    fun getAllTasks(): Flow<List<Task>> {
        return repo.getCompletedAll().map { tasks ->
            tasks.filter { it.status == true }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookApp).repo
                CompletedViewModel(repo)
            }
        }
    }
}