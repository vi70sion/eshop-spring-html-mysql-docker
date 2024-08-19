package com.example.eshop.controller;

import com.example.eshop.model.Order;
import com.example.eshop.service.OrderService;
import com.example.eshop.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class StripeController {

    @Autowired
    private StripeService stripeService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Order order) {
        try {
            Session session = stripeService.createCheckoutSession(order);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("id", session.getId());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/redirect")
    public RedirectView redirect(@RequestParam UUID uuid){
        return (orderService.setOrderPaymentStatusTrue(uuid)) ?
                new RedirectView("http://host.docker.internal:7777/thanks.html"): // status update successful
                new RedirectView("http://host.docker.internal:7777/errorPaying.html"); // status update failed
    }

}
