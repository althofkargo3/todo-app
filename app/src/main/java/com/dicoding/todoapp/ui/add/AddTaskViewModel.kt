package com.dicoding.todoapp.ui.add

import androidx.lifecycle.*
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> = _date

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setDate(date: Long) {
        _date.value = date
    }

    fun save() {
        val newTask = Task(
            title = title.value!!,
            description = description.value!!,
            dueDateMillis = date.value!!
        )
        viewModelScope.launch {
            taskRepository.insertTask(newTask)
        }
    }
}