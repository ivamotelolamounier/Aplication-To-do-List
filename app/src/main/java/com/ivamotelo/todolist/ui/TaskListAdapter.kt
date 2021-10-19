package com.ivamotelo.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivamotelo.todolist.R
import com.ivamotelo.todolist.databinding.ItemTaskBinding
import com.ivamotelo.todolist.model.Task

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallBack()) {

    /**
     * Variáveis globais para o evento onClickListener do menu suspenso da lista
     * (Editar ou Deletar), através de LAMBDAS
     */
    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * uso da inner class para que seja possível acessar os atributos da classe principal
     */
    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Função que mostra a tarefa selecionda para edição ou deleção, quando
         * clicado o 'menu de contexto' ivMore de cada item da lista de itens
         */
        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            binding.tvDescription.text = item.description
            binding.ivMore.setOnClickListener {
                showPopup(item)
            }
        }

        /**
         * Função para exibir um menu popup referente as opções de 'Editar' ou 'Deletar'
         * a tarefa que está sem foco, atraveś do popUpMenu.show(), inflando o popUpMenu,
         * que por sua vez possui o método setOnClickListener para capturar o clique do
         * usuário (Editar ou Deletar)
         */
        private fun showPopup(item: Task) {
            val ivMore = binding.ivMore
            val popUpMenu = PopupMenu(ivMore.context, ivMore)
            popUpMenu.menuInflater.inflate(R.menu.popup_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item) // recebe o parâmetro invocado no popup
                    R.id.action_delete -> listenerDelete(item) // recebe o parâmetro invocado no popup
                }
                return@setOnMenuItemClickListener true
            }
            popUpMenu.show()
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
}