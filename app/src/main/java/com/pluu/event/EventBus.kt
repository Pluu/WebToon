package com.pluu.event

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.channels.BroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.map
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

// https://gist.github.com/svenjacobs/57a21405b2dda4b62945c22235889d4a
object EventBus {
    private val channel = BroadcastChannel<Any>(1)

    fun send(event: Any, context: CoroutineContext = Dispatchers.Default) {
        GlobalScope.launch(context) {
            channel.send(event)
        }
    }

    fun subscribe(): ReceiveChannel<Any> = channel.openSubscription()

    inline fun <reified T> subscribeToEvent() =
        subscribe().filter { it is T }.map { it as T }
}
