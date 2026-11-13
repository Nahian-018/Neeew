package com.example.workflow.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.workflow.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkflowViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkflowRepository

    init {
        val dao = WorkflowDatabase.getDatabase(application).workflowDao()
        repository = WorkflowRepository(dao)
    }

    val workflows: StateFlow<List<WorkflowEntity>> = repository.allWorkflows
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var searchQuery = MutableStateFlow("")
        private set

    var selectedCategory = MutableStateFlow("All")
        private set

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    suspend fun getWorkflowById(id: Long): WorkflowEntity? {
        return repository.getWorkflowById(id)
    }

    fun addWorkflow(title: String, description: String, category: String, colorHex: String) {
        viewModelScope.launch {
            repository.insertWorkflow(
                WorkflowEntity(
                    title = title,
                    description = description,
                    category = category,
                    colorHex = colorHex
                )
            )
        }
    }

    fun updateWorkflow(workflow: WorkflowEntity) {
        viewModelScope.launch {
            repository.updateWorkflow(workflow)
        }
    }

    fun deleteWorkflow(workflow: WorkflowEntity) {
        viewModelScope.launch {
            repository.deleteWorkflow(workflow)
        }
    }

    fun addTask(workflowId: Long, title: String, description: String, priority: String, dueDate: Long) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    workflowId = workflowId,
                    title = title,
                    description = description,
                    priority = priority,
                    status = "To Do",
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateTaskStatus(task: TaskEntity, status: String) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = status))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addStep(taskId: Long, title: String) {
        viewModelScope.launch {
            repository.insertStep(StepEntity(taskId = taskId, title = title))
        }
    }

    fun toggleStep(step: StepEntity) {
        viewModelScope.launch {
            repository.updateStep(step.copy(isCompleted = !step.isCompleted))
        }
    }

    fun deleteStep(step: StepEntity) {
        viewModelScope.launch {
            repository.deleteStep(step)
        }
    }

    fun addNote(workflowId: Long, content: String) {
        viewModelScope.launch {
            repository.insertNote(NoteEntity(workflowId = workflowId, content = content))
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun getTasksForWorkflow(workflowId: Long) = repository.getTasksForWorkflow(workflowId)
    fun getStepsForTask(taskId: Long) = repository.getStepsForTask(taskId)
    fun getNotesForWorkflow(workflowId: Long) = repository.getNotesForWorkflow(workflowId)
}
