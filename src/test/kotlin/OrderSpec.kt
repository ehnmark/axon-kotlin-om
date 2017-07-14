package com.github.ehnmark.axonom

import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.Before
import org.junit.Test

class OrderSpec {
    lateinit var fixture: AggregateTestFixture<Order>

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(Order::class.java)
    }

    @Test
    fun testShouldNotAllowAmendBelowCumQty() {
        val id = createOrderId()
        fixture.given(
                    OrderCreatedEvent(id, 100),
                    OrderFilledEvent(id, 50)
                )
                .`when`(AmendOrderCommand(id, 40))
                .expectNoEvents()
                .expectException(InvalidAmendQuantityException::class.java)
    }

    @Test
    fun testShouldProcessFills() {
        val id = createOrderId()
        fixture.given(
                OrderCreatedEvent(id, 100),
                OrderFilledEvent(id, 50)
        )
                .`when`(FillOrderCommand(id, 40))
                .expectEvents(
                        OrderFilledEvent(id, 40)
                )
    }

    @Test
    fun testShouldReportOverfills() {
        val id = createOrderId()
        fixture.given(
                    OrderCreatedEvent(id, 100),
                    OrderFilledEvent(id, 50)
                )
                .`when`(FillOrderCommand(id, 60))
                .expectEvents(
                        OrderOverFilledEvent(id, 100, 110),
                        OrderFilledEvent(id, 60)
                )
    }
}