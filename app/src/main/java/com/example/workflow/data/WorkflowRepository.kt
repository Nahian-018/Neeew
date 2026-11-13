package com.example.workflow.data

import kotlinx.coroutines.flow.Flow

class WorkflowRepository(private val dao: WorkflowDao) {
    val allWorkflows: Flow<List<WorkflowEntity>> = dao.getAllWorkflows()
    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()

    suspend fun getWorkflowById(id: Long): WorkflowEntity? = dao.getWorkflowById(id)

    suspend fun insertWorkflow(workflow: WorkflowEntity) = dao.insertWorkflow(workflow)
    suspend fun updateWorkflow(workflow: WorkflowEntity) = dao.updateWorkflow(workflow)
    suspend fun deleteWorkflow(workflow: WorkflowEntity) = dao.deleteWorkflow(workflow)

    fun getTasksForWorkflow(workflowId: Long) = dao.getTasksForWorkflow(workflowId)
    suspend fun insertTask(task: TaskEntity) = dao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)

    fun getStepsForTask(taskId: Long) = dao.getStepsForTask(taskId)
    suspend fun insertStep(step: StepEntity) = dao.insertStep(step)
    suspend fun updateStep(step: StepEntity) = dao.updateStep(step)
    suspend fun deleteStep(step: StepEntity) = dao.deleteStep(step)

    fun getNotesForWorkflow(workflowId: Long) = dao.getNotesForWorkflow(workflowId)
    suspend fun insertNote(note: NoteEntity) = dao.insertNote(note)
    suspend fun deleteNote(note: NoteEntity) = dao.deleteNote(note)
}
