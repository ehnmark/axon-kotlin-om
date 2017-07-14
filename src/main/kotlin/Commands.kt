package com.github.ehnmark.axonom

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CreateOrderCommand(val clOrdId: OrderId, val ordQty: Long)
data class FillOrderCommand(@TargetAggregateIdentifier val clOrdId: OrderId, val lastQty: Long)
data class AmendOrderCommand(@TargetAggregateIdentifier val clOrdId: OrderId, val newOrdQty: Long)