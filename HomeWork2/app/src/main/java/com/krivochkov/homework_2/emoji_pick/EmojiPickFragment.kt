package com.krivochkov.homework_2.emoji_pick

import android.content.Context
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
import com.krivochkov.homework_2.utils.dpToPx

class EmojiPickFragment : BottomSheetDialogFragment() {

    private var _binding: EmojiPullLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EmojiPickAdapter

    private var listEmoji: List<String> = listOf()
    private var messageId: Long = -1
    private var onEmojiPickListener: OnEmojiPickListener? = null

    interface OnEmojiPickListener {
        fun onEmojiPick(messageId: Long, emoji: String)
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listEmoji = it.getStringArrayList(ARG_LIST_EMOJI)?.toList() ?: listOf()
            messageId = it.getLong(ARG_MESSAGE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EmojiPullLayoutBinding.bind(inflater.inflate(R.layout.emoji_pull_layout, container))
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEmojiPickListener) {
            onEmojiPickListener = context
        } else {
            throw RuntimeException(context.toString() )
        }
    }

    companion object {
        private const val COLLAPSED_HEIGHT = 228
        private const val SPAN_COUNT = 7
        private const val ARG_LIST_EMOJI = "ARG_LIST_EMOJI"
        private const val ARG_MESSAGE_ID = "ARG_MESSAGE_ID"

        @JvmStatic
        fun newInstance(listEmoji: List<String>, messageId: Long): EmojiPickFragment {
            return EmojiPickFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_LIST_EMOJI, ArrayList(listEmoji))
                    putLong(ARG_MESSAGE_ID, messageId)
                }
            }
        }
    }
}