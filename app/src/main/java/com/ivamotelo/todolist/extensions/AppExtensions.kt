/**
 * Função para inserir um comportamento para as datas do app
 *
 * Função para retornar a data no formato brasileiro (dia/mês/anos)
 *
 * Variável para retornar diretamente o get de um EditText
 * substituindo ('editText?.text?.toString :? : ""' ) com operador 'elvis' se o mesmo
 * retornar nulo, transformando-o em vazio
 * o set 'editText?.setText(value)' padronizando o código
*/
package com.ivamotelo.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import  java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

fun Date.format() : String {
    return SimpleDateFormat("dd/MM/yyyy", locale).format(this)
}

var TextInputLayout.text : String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }