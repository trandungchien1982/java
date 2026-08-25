package com.tdc.demo.controller;

import com.tdc.demo.annotation.CustomInstrumentLog;
import com.tdc.demo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @CustomInstrumentLog
    @GetMapping("/data")
    public String getData(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam String mainValue) {
        return orderService.getData(x, y, mainValue);
    }

    @CustomInstrumentLog
    @GetMapping("/calculate")
    public int calculate(
            @RequestParam int price,
            @RequestParam int quantity) {
        return orderService.calculateTotal(price, quantity);
    }

    @GetMapping("/notify")
    public String notifyOrder(@RequestParam long orderId) {
        orderService.notifyOrder(orderId);
        return "notified-" + orderId;
    }

    @GetMapping("/plain")
    public String plain(@RequestParam String value) {
        return orderService.methodWithoutAnnotation(value);
    }
}
