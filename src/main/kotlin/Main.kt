package com.github.ehnmark.axonom

import org.axonframework.config.DefaultConfigurer
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.commandhandling.SimpleCommandBus

fun main(args : Array<String>) {
    val config = DefaultConfigurer.defaultConfiguration()
            .configureAggregate(Order::class.java)
            .configureEmbeddedEventStore { InMemoryEventStorageEngine() }
            .configureCommandBus { SimpleCommandBus() }
            .buildConfiguration()
    config.start()
    val bus = config.commandBus()
    fun <T> sendCommand(cmd: T) {
        bus.dispatch(asCommandMessage<T>(cmd))
    }

    val id = createOrderId()
    sendCommand(CreateOrderCommand(id, 100))
    sendCommand(FillOrderCommand(id, 60))
    sendCommand(AmendOrderCommand(id, 50))
}