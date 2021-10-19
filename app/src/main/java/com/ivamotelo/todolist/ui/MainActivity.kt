/**
 * Utiliza-se o método 'binding' em substituição ao método 'findById', através da declaração
 * de uma variável global lateinit 'binding', do tipo ActivityMainBinding (ver documentação)
 * que será inflada como layoutInflater
 */
package com.ivamotelo.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ivamotelo.todolist.databinding.ActivityMainBinding
import com.ivamotelo.todolist.datasource.TaskDataSource
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Usa-se 'by lazy' para que o adapter aguarde o momento certo para ser instanciado
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // O adapter do recyclerView 'rvTask', receberá o adapter atual
        binding.rvTasks.adapter = adapter
        updateList()
        insertListeners()
        //DATA STORE
        //ROOM
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            // inicia a outra Activity "Addtask"
            startActivityForResult(Intent(this, AddTaskActivity:: class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            Log.d("TAG", "Menu PopUp EDIT + $it")

            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            Log.d("TAG", "Menu PopUp DELETE + $it")

            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    /**
     * recebe o resultado do cadastro realizado na Activity "AddtaskActivity"
     * verifica se o requestcode é de fato o mesmo enviado e se verdadeiras as condicões
     * entaão é realizado um 'updateList()'
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    /**
     * Atualiza a lista recebida e conferida na função onActivityResult e realiza um update
     * da lista, e se a imagem do gato estiver visível, a mesma será desabilitada (GONE)
     * isso feito, o adapter submete a lista
     */
    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE

        adapter.submitList(list)
    }
    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}