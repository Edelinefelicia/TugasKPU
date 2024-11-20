package com.example.tugaskpu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.database.Note
import com.example.tugaskpu.databinding.ItemListBinding

class PemilihAdapter(val listPemilih : List<Note>,
                     private val onDeleteClick: (Int) -> Unit,
                     private val onEditClick: (Int) -> Unit,
                     private val onViewClick: (Int) -> Unit

) : RecyclerView.Adapter<PemilihAdapter.ItemListViewHolder>()  {
    class ItemListViewHolder(private val binding:ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Note, position: Int,  onDeleteClick: (Int) -> Unit,
                     onEditClick: (Int) -> Unit,
                     onViewClick: (Int) -> Unit) {
                with(binding) {
                    txtAngka.text = (position + 1).toString()
                    txtNamapemilih.text = data.nama_pemilih

                    imgEdit.setOnClickListener{ onEditClick(position) }

                    imgDelete.setOnClickListener { onDeleteClick(position) }

                    imgSee.setOnClickListener { onViewClick(position) }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding =
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ItemListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPemilih.size
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(listPemilih[position], position,  onDeleteClick, onEditClick, onViewClick)
    }
}