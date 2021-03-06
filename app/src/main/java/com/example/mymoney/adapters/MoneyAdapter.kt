package com.example.mymoney.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.databinding.ExpenseRowLayoutBinding
import com.example.mymoney.models.ExpenseModel

class MoneyAdapter: RecyclerView.Adapter<MoneyAdapter.MyViewHolder>() {

    private var expenses: List<ExpenseModel> = emptyList()

    class MyViewHolder(val binding: ExpenseRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExpenseRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.expenseType.text = expenses[position].type
        holder.binding.expenseAmount.text = expenses[position].amount.toString()
        holder.binding.dateTime.text = expenses[position].date

    }

    override fun getItemCount(): Int {
        return expenses.size
    }


    fun setData(newData: List<ExpenseModel>) {
        this.expenses = newData
        notifyDataSetChanged()
    }
}