package com.example.mymoney.adapters

import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.CardRowLayoutBinding
import com.example.mymoney.ui.home.HomeFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CardAdapter(
    private val requireActivity: FragmentActivity
): RecyclerView.Adapter<CardAdapter.CardViewHolder>(), ActionMode.Callback {

    private var checked = false

    private var cards = emptyList<MoneyEntity>()

    private lateinit var mActionMode: ActionMode
    private var cardViewHolders = arrayListOf<CardViewHolder>()

    var id = 0L


    class CardViewHolder(val binding: CardRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardRowLayoutBinding.inflate(layoutInflater, parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val expenseBtn = requireActivity.findViewById<FloatingActionButton>(R.id.subtractMoneyFab)
        val incomeBtn = requireActivity.findViewById<FloatingActionButton>(R.id.addMoneyFab)
        val home = requireActivity.findViewById<ConstraintLayout>(R.id.homeLayout)

        val card = cards[position]
        holder.binding.moneyName.text = card.name

        holder.binding.moneyCard.setCardBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                card.color
            )
        )
        holder.binding.moneyCountTextView.text = card.value.toString()

        holder.binding.moneyCard.setOnClickListener{

            if (cardViewHolders.isNotEmpty() && id == position.toLong()) {
                showBtns(false)
                cardViewHolders.clear()
                mActionMode.finish()
                holder.binding.moneyCard.strokeWidth =
                    requireActivity.resources.getDimension(R.dimen.strokeWidth).toInt()
                holder.binding.moneyCard.invalidate()
            }
            else if (cardViewHolders.isNotEmpty() && id != position.toLong()) {
                cardViewHolders.first().binding.moneyCard.strokeWidth =
                    requireActivity.resources.getDimension(R.dimen.strokeWidth).toInt()
                cardViewHolders.first().binding.moneyCard.invalidate()
                cardViewHolders.clear()
                cardViewHolders.add(holder)
                id = position.toLong()
                holder.binding.moneyCard.strokeWidth =
                    requireActivity.resources.getDimension(R.dimen.strokeWidthChecked).toInt()
                mActionMode.title = holder.binding.moneyName.text.toString()
                holder.binding.moneyCard.invalidate()
            }
            else {
                holder.binding.moneyCard.strokeWidth =
                    requireActivity.resources.getDimension(R.dimen.strokeWidthChecked).toInt()
                showBtns(true)
                cardViewHolders.add(holder)
                id = position.toLong()
                Toast.makeText(requireActivity, id.toString(), Toast.LENGTH_SHORT).show()
                requireActivity.startActionMode(this)
                mActionMode.title = holder.binding.moneyName.text.toString()
            }

            expenseBtn.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(card)
                holder.itemView.findNavController().navigate(action)
            }

            incomeBtn.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetIncomeFragment(card)
                holder.itemView.findNavController().navigate(action)
            }
        }
        home.setOnClickListener {
            if (cardViewHolders.isNotEmpty()) {
                showBtns(false)
                //checked = false
                mActionMode.finish()
            }
        }
    }

    private fun showBtns(show: Boolean) {
        val expenseBtn = requireActivity.findViewById<FloatingActionButton>(R.id.subtractMoneyFab)
        val incomeBtn = requireActivity.findViewById<FloatingActionButton>(R.id.addMoneyFab)
        if (show) {
            expenseBtn.visibility = View.VISIBLE
            incomeBtn.visibility = View.VISIBLE
        }
        else {
            expenseBtn.visibility = View.INVISIBLE
            incomeBtn.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return cards.size
    }

    fun setData(newCards: List<MoneyEntity>) {
        this.cards = newCards
        notifyDataSetChanged()
    }

    override fun onCreateActionMode(actionMode: ActionMode?, p1: Menu?): Boolean {
        mActionMode = actionMode!!
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, p1: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, p1: MenuItem?): Boolean {
        actionMode?.finish()

        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        cardViewHolders.forEach { card ->
            card.binding.moneyCard.strokeWidth =
                requireActivity.resources.getDimension(R.dimen.strokeWidth).toInt()
            card.binding.moneyCard.invalidate()

        }
        showBtns(false)
        cardViewHolders.clear()

    }


}