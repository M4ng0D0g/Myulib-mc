package com.myudog.myulib.internal.event

import com.myudog.myulib.api.event.Event
import com.myudog.myulib.api.event.EventPriority
import com.myudog.myulib.api.event.ProcessResult
import com.myudog.myulib.api.event.listener.EventListener
import kotlin.reflect.KClass

private class PrioritizedListener<T : Event>(
    val priority: UInt,
    val listener: EventListener<T>
) : Comparable<PrioritizedListener<T>> {
    override fun compareTo(other: PrioritizedListener<T>): Int {
        return this.priority.compareTo(other.priority)
    }
}

/**
 * 改為 open class，允許被實例化。不再使用 object 全域綁死。
 */
open class EventDispatcherImpl {
    // 每個實例都有自己獨立的 listeners Map
    private val listeners = mutableMapOf<KClass<out Event>, MutableList<PrioritizedListener<out Event>>>()

    inline fun <reified T : Event> subscribe(
        priority: UInt = EventPriority.NORMAL,
        listener: EventListener<T>
    ): EventListener<T> {
        return subscribe(T::class, priority, listener)
    }

    fun <T : Event> subscribe(
        eventType: KClass<T>,
        priority: UInt,
        listener: EventListener<T>
    ): EventListener<T> {
        val list = listeners.getOrPut(eventType) { mutableListOf() }
        list.add(PrioritizedListener(priority, listener))

        @Suppress("UNCHECKED_CAST")
        (list as MutableList<PrioritizedListener<T>>).sort()

        return listener
    }

    inline fun <reified T : Event> unsubscribe(listener: EventListener<T>) {
        unsubscribe(T::class, listener)
    }

    fun <T : Event> unsubscribe(eventType: KClass<T>, listener: EventListener<T>) {
        listeners[eventType]?.removeIf { it.listener == listener }
    }

    fun dispatch(event: Event): ProcessResult {
        val eventType = event::class
        val list = listeners[eventType] ?: return ProcessResult.PASS

        for (wrapper in list) {
            @Suppress("UNCHECKED_CAST")
            val typedWrapper = wrapper as PrioritizedListener<Event>
            val result = typedWrapper.listener.handle(event)
            if (result != ProcessResult.PASS) {
                return result
            }
        }
        return ProcessResult.PASS
    }
}