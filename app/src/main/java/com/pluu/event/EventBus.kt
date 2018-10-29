package com.pluu.event

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.filter
import kotlinx.coroutines.channels.map
import kotlin.coroutines.CoroutineContext

// https://gist.github.com/svenjacobs/57a21405b2dda4b62945c22235889d4a
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
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
