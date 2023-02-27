package com.ramlin.observeme.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    private final ItemRepository itemRepository;
    private final MeterRegistry registry;
    private Counter counter;
    public ItemController(ItemRepository itemRepository, MeterRegistry registry) {
        this.itemRepository = itemRepository;
        this.registry = registry;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Integer id) {
        counter = registry.counter("items.total", "id", id.toString());
        counter.increment();

        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Item> saveItem(@PathVariable Integer id) {
        itemRepository.save(new Item(id,"ITEM"+id));

        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}