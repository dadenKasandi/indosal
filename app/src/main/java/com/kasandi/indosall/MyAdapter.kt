package com.kasandi.indosall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userList: ArrayList<Lapangan>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvNamaLapangan.text = userList[position].nama_lapangan
        holder.tvAlamatLapangan.text = userList[position].alamat_lapangan
        holder.tvRatingLapangan.text = userList[position].rate_lapangan
        // Bind data to ViewHolder
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare and initialize views in ViewHolder
        val tvNamaLapangan: TextView = itemView.findViewById(R.id.tv_item_nama_tempat)
        val tvAlamatLapangan: TextView = itemView.findViewById(R.id.tv_item_lokasi)
        val tvRatingLapangan: TextView = itemView.findViewById(R.id.tv_item_rate)
    }
}
