package com.example.bdmcrudrev.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bdmcrudrev.R
import com.example.bdmcrudrev.`object`.EmpModel

class EmpAdapter(
    private var itemsCells: ArrayList<EmpModel>,
    private val clickListener: (EmpModel) -> Unit) :
    RecyclerView.Adapter<EmpAdapter.ViewHolder>() {

    //insialisasi untuk list yang akan dibuat menggunakan RecylerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewId = itemView.findViewById(R.id.textViewId) as TextView
        val textViewName = itemView.findViewById(R.id.textViewName) as TextView
        val textViewEmail = itemView.findViewById(R.id.textViewEmail) as TextView
        val textViewAddress = itemView.findViewById(R.id.textViewAddress) as TextView
        //fun untuk memanggil data ketika item diklik
        fun bind(part: EmpModel, clickListener: (EmpModel) -> Unit) {
            itemView.setOnClickListener { clickListener(part) }
        }
    }

    //fun menampilkan data ke tampilan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list, parent, false)
        return ViewHolder(itemView)
    }

    //fun untuk menghitung keseluruhan data
    override fun getItemCount(): Int {
        return itemsCells.size
    }

    //fun menampilkan data yang diklik
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsCells[position], clickListener)
        val user: EmpModel = itemsCells[position]
        holder.textViewId.text = user.userId.toString()
        holder.textViewName.text = user.userName
        holder.textViewEmail.text = user.userEmail
        holder.textViewAddress.text = user.userAddress
    }
}