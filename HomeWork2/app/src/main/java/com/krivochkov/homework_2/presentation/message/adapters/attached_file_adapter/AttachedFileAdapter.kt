package com.krivochkov.homework_2.presentation.message.adapters.attached_file_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.InputFileItemBinding
import com.krivochkov.homework_2.domain.models.AttachedFile

class AttachedFileAdapter(
    private val onCancelClick: (file: AttachedFile) -> Unit
) : RecyclerView.Adapter<AttachedFileAdapter.FileViewHolder>() {

    private val differ: AsyncListDiffer<AttachedFile> = AsyncListDiffer(this, DiffCallback())

    var files: List<AttachedFile>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private class DiffCallback : DiffUtil.ItemCallback<AttachedFile>() {

        override fun areItemsTheSame(oldItem: AttachedFile, newItem: AttachedFile) =
            areContentsTheSame(oldItem, newItem)

        override fun areContentsTheSame(oldItem: AttachedFile, newItem: AttachedFile) =
            oldItem == newItem
    }

    class FileViewHolder(
        private val binding: InputFileItemBinding,
        private val onCancelClick: (file: AttachedFile) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attachedFile: AttachedFile) {
            binding.apply {
                fileName.text = attachedFile.name
                cancelButton.setOnClickListener { onCancelClick(attachedFile) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            InputFileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCancelClick = onCancelClick
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}