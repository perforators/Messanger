package com.krivochkov.homework_2.presentation.chat.emoji_pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentEmojiPickBinding
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.utils.dpToPx


class EmojiPickFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEmojiPickBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EmojiPickAdapter

    private var listEmoji: List<Emoji> = EmojiProvider.getAll()
    private var messageId: Long = -1
    private var onEmojiPickListener: OnEmojiPickListener? = null

    interface OnEmojiPickListener {
        fun onEmojiPick(messageId: Long, emoji: Emoji)
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEmojiPickListener = parentFragment as? OnEmojiPickListener
        requireArguments().apply {
            messageId = getLong(ARG_MESSAGE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiPickBinding
            .bind(inflater.inflate(R.layout.fragment_emoji_pick, container))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        adapter = EmojiPickAdapter(listEmoji) {
            onEmojiPickListener?.onEmojiPick(messageId, it)
            dismiss()
        }
        binding.emojiPullRecycler.adapter = adapter
        binding.emojiPullRecycler.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
    }


    override fun onStart() {
        super.onStart()

        dialog?.let {
            val bottomSheet = it.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = COLLAPSED_HEIGHT.dpToPx(requireContext()).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        private const val COLLAPSED_HEIGHT = 228
        private const val SPAN_COUNT = 7
        private const val ARG_MESSAGE_ID = "ARG_MESSAGE_ID"

        @JvmStatic
        fun newInstance(messageId: Long): EmojiPickFragment {
            return EmojiPickFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_MESSAGE_ID, messageId)
                }
            }
        }
    }
}
