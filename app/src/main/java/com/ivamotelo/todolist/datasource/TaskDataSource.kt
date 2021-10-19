package com.ivamotelo.todolist.datasource

import com.ivamotelo.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    /**
     * Se for uma nova tarefa, apena adiciona a mesma Add(Copy...)
     * se for uma edição, remove a tarefa editda e cria uma nova  id reeditada
     * (remove o id (task) em seguida add o id (task)
     */
    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task) {
        list.remove(task)
    }
}