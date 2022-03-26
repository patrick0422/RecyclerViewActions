package com.example.recyclerviewactions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewactions.databinding.ItemListBinding
import java.util.Collections
import kotlin.collections.ArrayList

class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
    private var itemList = arrayListOf<String>()

    fun setData(newList: List<String>) {
        DiffUtil.calculateDiff(BaseDiffUtil(itemList, newList)).let {
            itemList = ArrayList(newList)
            it.dispatchUpdatesTo(this)
        }
    }

    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(itemList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    fun insertDataAt(pos: Int, item: String) {
        itemList.add(pos, item)
        notifyItemInserted(pos)
    }

    fun dataAt(pos: Int) = itemList[pos]

    fun removeDataAt(pos: Int) {
        itemList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder.binding) {
        textView.text = itemList[position]
        imageButton.setOnClickListener {
            Toast.makeText(this.root.context, itemList[position], Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder.from(parent)

    override fun getItemCount(): Int = itemList.size
}

class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup) = MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list,
                parent,
                false
            )
        )
    }

}
