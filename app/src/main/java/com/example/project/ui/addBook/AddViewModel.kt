package com.example.project.ui.addBook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project.BookApp
import com.example.project.data.model.Task
import com.example.project.data.repository.BookRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class AddViewModel(
    private val repo: BookRepo
) : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
    val mean: MutableLiveData<String> = MutableLiveData()
    val syn: MutableLiveData<String> = MutableLiveData()
    val detail: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun submit() {
        viewModelScope.launch (Dispatchers.IO){
        if (title.value != null && mean.value != null && syn.value != null && detail.value != null) {
            repo.addTask(Task(title = title.value!!, mean = mean.value!!, syn = syn.value!!, detail = detail.value!!, status = false))

            finish.emit(Unit)
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[APPLICATION_KEY] as BookApp).repo
                AddViewModel(repo)
            }
        }
    }
}