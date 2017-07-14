package com.github.ehnmark.axonom

import java.util.*

data class OrderId(val id: String)

fun createOrderId() = OrderId(UUID.randomUUID().toString())