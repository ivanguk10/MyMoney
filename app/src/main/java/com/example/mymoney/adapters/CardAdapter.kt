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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CardAdapter(
    private val requireActivity: FragmentActivity
): RecyclerView.Adapter<CardAdapter.CardViewHolder>(), ActionMode.Callback {

    private var checked = false

    private var cards = emptyList<MoneyEntity>()

    private lateinit var mActionMode: ActionMode
    private var cardViewHolders = arrayListOf<CardViewHolder>()

    private var id = 0


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

            if (cardViewHolders.isNotEmpty() && id == position) {
                showBtns(false)
                cardViewHolders.clear()
                mActionMode.finish()
                strokeWidth(holder, R.dimen.strokeWidth)
                holder.binding.moneyCard.invalidate()
            }
            else if (cardViewHolders.isNotEmpty() && id != position) {
                strokeWidth(cardViewHolders.first(), R.dimen.strokeWidth)
                cardViewHolders.first().binding.moneyCard.invalidate()
                cardViewHolders.clear()
                cardViewHolders.add(holder)
                id = position
                strokeWidth(holder, R.dimen.strokeWidthChecked)
                mActionMode.title = holder.binding.moneyName.text.toString()
                holder.binding.moneyCard.invalidate()
            }
            else {
                strokeWidth(holder, R.dimen.strokeWidthChecked)
                showBtns(true)
                cardViewHolders.add(holder)
                id = position
                requireActivity.startActionMode(this)
                mActionMode.title = holder.binding.moneyName.text.toString()
            }

            expenseBtn.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(card)
                holder.itemView.findNavController().navigate(action)
                mActionMode.finish()
            }

            incomeBtn.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetIncomeFragment(card)
                holder.itemView.findNavController().navigate(action)
                mActionMode.finish()
            }
        }
        home.setOnClickListener {
            if (cardViewHolders.isNotEmpty()) {
                showBtns(false)
                mActionMode.finish()
            }
        }
    }

    private fun showBtns(show: Boolean) {
        val expenseBtn = requireActivity.findViewById<FloatingActionButton>(R.id.subtractMoneyFab)
        val incomeBtn = requireActivity.findViewById<FloatingActionButton>(R.id.addMoneyFab)
        if (show) {
            expenseBtn?.isEnabled = true
            incomeBtn?.isEnabled = true
            expenseBtn.visibility = View.VISIBLE
            incomeBtn.visibility = View.VISIBLE
            expenseBtn?.animate()?.alpha(1f)?.duration = 700
            incomeBtn?.animate()?.alpha(1f)?.duration = 700
        }
        else {
            expenseBtn?.animate()?.alpha(0f)?.duration = 700
            incomeBtn?.animate()?.alpha(0f)?.duration = 700
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                expenseBtn?.isEnabled = false
                incomeBtn?.isEnabled = false
                expenseBtn?.visibility = View.INVISIBLE
                incomeBtn?.visibility = View.INVISIBLE
            }

        }
    }

    private fun strokeWidth(holder: CardViewHolder, width: Int) {
        holder.binding.moneyCard.strokeWidth =
            requireActivity.resources.getDimension(width).toInt()
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
            strokeWidth(card, R.dimen.strokeWidth)
            card.binding.moneyCard.invalidate()
        }
        showBtns(false)
        cardViewHolders.clear()
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun clearActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }


}