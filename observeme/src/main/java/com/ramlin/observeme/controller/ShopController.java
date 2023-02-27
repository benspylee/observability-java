package com.ramlin.observeme.controller;


import com.ramlin.observeme.models.Item;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Random;


@RestController
public class ShopController {
    private Counter itemListCounter;
    private Timer itemRequestTimer;

    private MeterRegistry meterRegistry;

    public ShopController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        itemListCounter = meterRegistry
                .counter("PAGE_VIEWS.ItemList");
        itemRequestTimer = meterRegistry
                .timer("execution.time.fetchItems");
    }

    private List<Item> itemList  =  List.of(
            new Item("ITM001","tomato",10,20),
            new Item("ITM002","corrot",10,20),
            new Item("ITM003","beet-root",10,20)
    );

    private int getRandom(){
        return new Random().nextInt(5) + 1;

    }

    @GetMapping("/items")
    public List<Item> itemList() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        itemListCounter.increment();
        Thread.sleep(1000*getRandom());
        itemRequestTimer.record(Duration.ofMillis(System.currentTimeMillis() - startTime));
        return itemList;
    }

}
