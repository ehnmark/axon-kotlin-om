package com.github.ehnmark.axonom

data class OrderCreatedEvent(val clOrdId: OrderId, val ordQty: Long)
data class OrderFilledEvent(val clOrdId: OrderId, val lastQty: Long)
data class OrderOverFilledEvent(val clOrdId: OrderId, val ordQty: Long, val cumQty: Long)
data class OrderAmendedEvent(val clOrdId: OrderId, val newOrdQty: Long)