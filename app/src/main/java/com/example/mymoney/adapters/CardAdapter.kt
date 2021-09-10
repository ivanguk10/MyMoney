package com.example.mymoney.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.CardRowLayoutBinding

class CardAdapter(
    private val requireActivity: FragmentActivity
): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var cards = emptyList<MoneyEntity>()

    class CardViewHolder(val binding: CardRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardRowLayoutBinding.inflate(layoutInflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.binding.moneyName.text = cards[position].name
        holder.binding.moneyCard.setCardBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                cards[position].color
            )
        )
        holder.binding.moneyCountTextView.text = cards[position].value.toString()
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    fun setData(newCards: List<MoneyEntity>) {
        this.cards = newCards
        notifyDataSetChanged()
    }
}