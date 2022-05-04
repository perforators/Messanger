package com.krivochkov.homework_2

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.krivochkov.homework_2.presentation.MainActivity
import org.junit.Test
import com.krivochkov.homework_2.screen.SubscribedChannelsScreen

class TopicListTest : TestCase() {

    @Test
    fun showTopicsForFirstChannel_ByDefault() = run {
        ActivityScenario.launch(MainActivity::class.java)

        // задержка для того, чтобы пришли данные с бека
        Thread.sleep(3000)

        step("Проверка, что количество элементов в списке равно 4") {
            SubscribedChannelsScreen.itemsList.hasSize(4)
        }
        step("Проверка, что список топиков скрыт для первого канала") {
            SubscribedChannelsScreen.itemsList.childAt<SubscribedChannelsScreen.ChannelItem>(0) {
                arrow {
                    hasDrawable(R.drawable.arrow_down_picture)
                }
            }
        }
        step("Клик по первому каналу") {
            SubscribedChannelsScreen.itemsList.childAt<SubscribedChannelsScreen.ChannelItem>(0) {
                click()
            }
        }
        step("Проверка, что список топиков для первого канала открылся") {
            SubscribedChannelsScreen.itemsList.childAt<SubscribedChannelsScreen.ChannelItem>(0) {
                arrow {
                    hasDrawable(R.drawable.arrow_up_picture)
                }
            }
        }
        step("Проверка, что количество элементов стало равным 8") {
            SubscribedChannelsScreen.itemsList.hasSize(8)
        }
    }
}