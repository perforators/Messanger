package com.krivochkov.homework_2.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels.SubscribedChannelsFragment

object SubscribedChannelsScreen : KScreen<SubscribedChannelsScreen>() {

    override val layoutId: Int = R.layout.fragment_subscribed_channels
    override val viewClass: Class<*> = SubscribedChannelsFragment::class.java

    val itemsList = KRecyclerView(
        builder = { withId(R.id.channels_recycler_view) },
        itemTypeBuilder = { itemType { ChannelItem(it) } }
    )

    class ChannelItem(parent: Matcher<View>) : KRecyclerItem<ChannelItem>(parent) {
        val arrow = KImageView(parent) { withId(R.id.arrow) }
    }
}