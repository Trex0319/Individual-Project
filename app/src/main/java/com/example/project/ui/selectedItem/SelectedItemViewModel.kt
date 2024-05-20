package com.example.project.ui.selectedItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project.BookApp
import com.example.project.data.model.Task
import com.example.project.data.repository.BookRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SelectedItemViewModel(
    private val repo: BookRepo
) : ViewModel() {

    private val _task: MutableLiveData<Task> = MutableLiveData()
    val selectedTask: LiveData<Task> = _task
    val finish :MutableSharedFlow<Unit> = MutableSharedFlow()
    val title: MutableLiveData<String> = MutableLiveData()
    val mean: MutableLiveData<String> = MutableLiveData()
    val syn: MutableLiveData<String> = MutableLiveData()
    val detail: MutableLiveData<String> = MutableLiveData()

    fun getTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _task.postValue(repo.getTaskById(id))
        }
    }

    fun moveTaskToCompleted(){
        viewModelScope.launch(Dispatchers.IO) {
            val currentTask = selectedTask.value
            if (currentTask != null) {
                val updatedStatus = !currentTask.status!!
                val updatedTask = currentTask.copy(status = updatedStatus)
                repo.updateTask(updatedTask)
                finish.emit(Unit)
            }
        }
    }

    fun delete() {
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteTask(selectedTask.value!!)
            finish.emit(Unit)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookApp).repo
                SelectedItemViewModel(repo)
            }
        }
    }
}