package com.voxcom.nextpill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(private val list: MutableList<MedicineInfo>) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val dosageText: TextView = itemView.findViewById(R.id.dosageText)
        val frequencyText: TextView = itemView.findViewById(R.id.frequencyText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val item = list[position]
        holder.nameText.text = "${item.name}"
        holder.dosageText.text = "${item.dosage}"
        holder.frequencyText.text = "Frequency: ${item.frequency}"
    }

    override fun getItemCount(): Int = list.size

    fun addItem(medicine: MedicineInfo) {
        list.add(medicine)
        notifyItemInserted(list.size - 1)
    }
}
