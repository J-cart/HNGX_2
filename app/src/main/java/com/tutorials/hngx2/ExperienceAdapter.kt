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
import com.tutorials.hngx2.databinding.ExperienceViewholderBinding
import com.tutorials.hngx2.databinding.SkillsViewholderBinding
import com.tutorials.hngx2.model.Experience

class ExperienceAdapter(private val isEditable: Boolean) : ListAdapter<Experience, ExperienceAdapter.ViewHolder>(diffObject) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ExperienceViewholderBinding.bind(view)

        fun bind(experience: Experience) {
            binding.apply {
                if (isEditable){
                    binding.removeBtn.visibility = View.VISIBLE
                }else{
                    binding.removeBtn.visibility = View.GONE
                }
                jobTitleText.text = experience.jobTitle
                companyText.text = experience.company.ifEmpty { "Nil" }
                dateText.text = experience.date.ifEmpty { "---" }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.experience_viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        if (pos != null)
            holder.bind(pos)

    }

    companion object {
        val diffObject = object : DiffUtil.ItemCallback<Experience>() {
            override fun areItemsTheSame(oldItem: Experience, newItem: Experience): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Experience, newItem: Experience): Boolean {
                return oldItem.company == newItem.company && oldItem.date == newItem.date
            }
        }
    }

    private var listener: ((Experience) -> Unit)? = null

    fun adapterClickListener(listener: (Experience) -> Unit) {
        this.listener = listener
    }

}
