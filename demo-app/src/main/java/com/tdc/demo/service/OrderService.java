package com.tdc.demo.service;

import com.tdc.demo.annotation.CustomInstrumentLog;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @CustomInstrumentLog
    public String getData(int x, int y, String mainValue) {
        return mainValue + "-" + (x + y);
    }

    @CustomInstrumentLog
    public int calculateTotal(int price, int quantity) {
        return price * quantity;
    }

    @CustomInstrumentLog
    public void notifyOrder(long orderId) {
        // Demo void method. Agent logs return value as <void>.
    }

    public String methodWithoutAnnotation(String value) {
        return "NO-AGENT-LOG-" + value;
    }
}
