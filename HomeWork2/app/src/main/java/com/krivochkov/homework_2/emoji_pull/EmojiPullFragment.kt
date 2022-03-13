package com.krivochkov.homework_2.emoji_pull

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.EmojiPullLayoutBinding

class EmojiPullFragment(
    private val listEmoji: List<String>,
    private val onEmojiClick: (String) -> Unit
) : BottomSheetDialogFragment() {

    lateinit var binding: EmojiPullLayoutBinding
    private lateinit var adapter: EmojiPullAdapter

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmojiPullLayoutBinding.bind(inflater.inflate(R.layout.emoji_pull_layout, container))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        adapter = EmojiPullAdapter(listEmoji) {
            onEmojiClick(it)
            dismiss()
        }
        binding.emojiPullRecycler.adapter = adapter
        binding.emojiPullRecycler.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
    }


    override fun onStart() {
        super.onStart()

        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            val bottomSheet = it.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        private const val COLLAPSED_HEIGHT = 228
        private const val SPAN_COUNT = 7

        fun createInstance(
            listEmoji: List<String>,
            onEmojiClick: (String) -> Unit
        ): EmojiPullFragment = EmojiPullFragment(listEmoji, onEmojiClick)
    }
}