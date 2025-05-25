
package com.example.demo.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrder")
    public Map<String, Object> getOrder(@PathVariable String id) {
        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated downstream service failure");
        }
        Map<String, Object> order = new HashMap<>();
        order.put("id", id);
        order.put("status", "confirmed");
        order.put("total", 123.45);
        return order;
    }

    public Map<String, Object> fallbackOrder(String id, Throwable t) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("id", id);
        fallback.put("status", "pending");
        fallback.put("message", "Fallback: service temporarily unavailable");
        return fallback;
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> payload) {
        payload.put("id", UUID.randomUUID().toString());
        payload.put("status", "processing");
        return payload;
    }
}
