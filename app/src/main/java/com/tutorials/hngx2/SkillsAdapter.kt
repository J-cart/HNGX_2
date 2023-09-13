package com.tutorials.hngx2

import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorials.hngx2.databinding.SkillsViewholderBinding

class SkillsAdapter(private val isEditable: Boolean) : ListAdapter<String, SkillsAdapter.ViewHolder>(diffObject) {
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
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.length == newItem.length && oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

    private var listener: ((String) -> Unit)? = null

    fun adapterClickListener(listener: (String) -> Unit) {
        this.listener = listener
    }

}
