/** binding.tilDate.editText?.setText(Date(it).format())
 * -> forma convencional pela -> forma por padronização na classe extensions.kt
 * que substituiu 'editText?.setText(Date(it).format())' por 'text'
 * Tamém é necessário a criação das val 'timezone' e 'offSet' para capturar
 * a data atual corretamente.
 * Faz o tratamento da hora e minuto para acrescentar '0' se a hora e o minuto estiver
 * com uma casa decimal (entre 0 e 9) horas ou minutos, através val 'hours' e val 'minute'
 * subsitituindo o timepicker pelas variáveis tratadas
 */
package com.ivamotelo.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivamotelo.todolist.databinding.ActivityAddTaskBinding
import com.ivamotelo.todolist.datasource.TaskDataSource
import com.ivamotelo.todolist.extensions.format
import com.ivamotelo.todolist.extensions.text
import com.ivamotelo.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import android.util.Log

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
                binding.tilDescription.text = it.description
            }
        }

        insertListners()
    }

    private fun insertListners() {
        binding.tilDate.editText?.setOnClickListener {
            Log.d("TAG", "setOnClickerListeners -> Data")
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
                Log.d("TAG", "PositiveButtonClickListener -> Data")
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            Log.d("TAG", "setOnClickerListener -> Horas")
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hours = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilHour.text = "$hours:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        /**
         * Verifica se é uma nova tarefa ou se é uma tarefa editada
         * pelo 'id', uma vez que ao se editar uma tarefa, este 'id' é modificado
         * isso evita a duplicação de uma tarefe que ao ser editada, pode se cadastrada
         * como uma nova e não como uma atualizaçao (nova = 0) e (editção = size+1)
         */
        binding.btnNewTask.setOnClickListener {
            val task = Task (
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                description = binding.tilDescription.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            Log.e("TAG", "insertTask" + TaskDataSource.getList())
            setResult(Activity.RESULT_OK)
            /**
             * finaliza esta Activity ao confirmar o cadastro da tarefa na
             * Activity TaskListAdapter, voltando para a Activity principal
             */
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}

