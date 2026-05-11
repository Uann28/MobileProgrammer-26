package com.example.tugasakhirpasien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpasien.R
import com.example.tugasakhirpasien.model.Pasien

class PasienAdapter(
    private val onEditClick: (Pasien) -> Unit,
    private val onDeleteClick: (Pasien) -> Unit
) : RecyclerView.Adapter<PasienAdapter.PasienViewHolder>() {

    private val listPasien = mutableListOf<Pasien>()

    fun setData(data: List<Pasien>) {
        listPasien.clear()
        listPasien.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasienViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pasien, parent, false)
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasienViewHolder, position: Int) {
        holder.bind(listPasien[position])
    }

    override fun getItemCount(): Int = listPasien.size

    inner class PasienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvNamaPasien)
        private val tvDetail: TextView = itemView.findViewById(R.id.tvDetailPasien)
        private val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamatTelp)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(pasien: Pasien) {
            tvNama.text = pasien.nama
            tvDetail.text = "L/P: ${pasien.jenis_kelamin} | Tgl Lahir: ${pasien.tanggal_lahir}"
            tvAlamat.text = "${pasien.alamat} | Telp: ${pasien.no_telepon}"

            btnEdit.setOnClickListener { onEditClick(pasien) }
            btnDelete.setOnClickListener { onDeleteClick(pasien) }
        }
    }
}