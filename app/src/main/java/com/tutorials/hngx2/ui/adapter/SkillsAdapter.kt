package com.tutorials.hngx2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorials.hngx2.R
import com.tutorials.hngx2.databinding.SkillsViewholderBinding

class SkillsAdapter(private val isEditable: Boolean) : ListAdapter<String, SkillsAdapter.ViewHolder>(
    diffObject
) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = SkillsViewholderBinding.bind(view)

        fun bind(skill: String) {
            binding.apply {
                if (isEditable){
                    binding.removeBtn.visibility = View.VISIBLE
                }else{
                    binding.removeBtn.visibility = View.GONE
                }
                skillText.text = skill
                removeBtn.setOnClickListener {
                    removeListener?.let { it(skill) }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.skills_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        if (pos != null)
            holder.bind(pos)

    }

    companion object {
        val diffObject = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.count() == newItem.count()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.length == newItem.length && oldItem.count() == newItem.count()
            }
        }
    }

    private var removeListener: ((String) -> Unit)? = null

    fun adapterDeleteClickListener(listener: (String) -> Unit) {
        this.removeListener = listener
    }

}
