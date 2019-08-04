package com.pluu.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// https://gist.github.com/svenjacobs/57a21405b2dda4b62945c22235889d4a
@ExperimentalCoroutinesApi
object EventBus {

    private val channel = BroadcastChannel<Any>(1)

    fun send(event: Any, context: CoroutineContext = Dispatchers.Default) {
        GlobalScope.launch(context) {
            channel.send(event)
        }
    }

    @FlowPreview
    fun subscribe(): Flow<Any> = channel.asFlow()

    @FlowPreview
    inline fun <reified T> subscribeToEvent() =
        subscribe().filter { it is T }.map { it as T }
}
