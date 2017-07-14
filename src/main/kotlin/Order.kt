package com.github.ehnmark.axonom

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler

class InvalidAmendQuantityException(msg: String) : Exception(msg) {}

class Order() {
    @CommandHandler
    constructor(cmd: CreateOrderCommand) : this() {
        apply(OrderCreatedEvent(cmd.clOrdId, cmd.ordQty))
    }

    @AggregateIdentifier private var clOrdId: OrderId? = null
    private var ordQty: Long = 0L
    private var cumQty: Long = 0L

    @CommandHandler
    fun handle(cmd: AmendOrderCommand) {
        if(cmd.newOrdQty < cumQty) throw InvalidAmendQuantityException("Cannot amend below filled qty")
        else apply(OrderAmendedEvent(cmd.clOrdId, cmd.newOrdQty))
    }

    @CommandHandler
    fun handle(cmd: FillOrderCommand) {
        if(cmd.lastQty + cumQty > ordQty)
            apply(OrderOverFilledEvent(cmd.clOrdId, ordQty, cumQty + cmd.lastQty))
        apply(OrderFilledEvent(cmd.clOrdId, cmd.lastQty))
    }

    @EventSourcingHandler
    fun on(evt: OrderCreatedEvent) {
        clOrdId = evt.clOrdId
        ordQty = evt.ordQty
    }

    @EventSourcingHandler
    fun on(evt: OrderFilledEvent) {
        cumQty += evt.lastQty
    }

    @EventSourcingHandler
    fun on(evt: OrderAmendedEvent) {
        ordQty = evt.newOrdQty
    }

}