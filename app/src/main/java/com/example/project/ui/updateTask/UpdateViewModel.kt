package com.example.project.ui.updateTask

import android.util.Log
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
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class UpdateViewModel(
    private val repo: BookRepo
) : ViewModel() {
    private val _task : MutableLiveData<Task> = MutableLiveData()
    val selectedTask: LiveData<Task> = _task
    val title: MutableLiveData<String> = MutableLiveData()
    val mean: MutableLiveData<String> = MutableLiveData()
    val syn: MutableLiveData<String> = MutableLiveData()
    val detail: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun getTaskById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _task.postValue(repo.getTaskById(id))
        }
    }

    fun setTask(task: Task) {
        task?. let {
            title.value =it.title
            mean.value= it.mean
            detail.value = it.detail
            syn.value= it.syn
        }

    }

    fun update() {
        if (title.value.isNullOrEmpty() || mean.value.isNullOrEmpty() || syn.value.isNullOrEmpty() || detail.value.isNullOrEmpty()) {
            snackbar.value = "All fields cannot be empty"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val task = selectedTask.value
            if (task != null) {
                repo.updateTask(
                    task.copy(
                        title = title.value!!,
                        mean = mean.value!!,
                        syn = syn.value!!,
                        detail = detail.value!!,
                        date = LocalDateTime.now()
                    )
                )
                withContext(Dispatchers.Main) {
                    snackbar.value = "Update Successfully"
                }
            }
            finish.emit(Unit)
        }
        Log.d("update","${title.value}, ${mean.value}, ${syn.value}, ${detail.value}")

    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookApp).repo
                UpdateViewModel(repo)
            }
        }
    }
}