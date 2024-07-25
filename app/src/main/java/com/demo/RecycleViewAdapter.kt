package com.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.demo.databinding.ItemClockBinding

class RecycleViewAdapter(private var timers: List<TimerItem>): RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemClockBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClockBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timer = timers[position]
        with(holder.binding) {
            tvTimer.text = timer.elapsesTimeString
            round.text = "Vòng ${timer.round}"
        }
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    fun updateTimers(newTimers: List<TimerItem>) {
        timers = newTimers
        notifyDataSetChanged()
    }
}