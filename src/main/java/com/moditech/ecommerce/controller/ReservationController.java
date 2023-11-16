package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.dto.ProductDto;
import com.moditech.ecommerce.model.Order;
import com.moditech.ecommerce.model.Reservation;
import com.moditech.ecommerce.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin("*")
public class ReservationController {


    @Autowired
    ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<Reservation> createProduct(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Reservation>> getReservationList() {
        List<Reservation> reservation = reservationService.getReservationList();
        return ResponseEntity.ok(reservation);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable String id, @RequestBody Reservation reservation){
        Reservation updateReservation = reservationService.updateReservationStatus(id, reservation);
        return ResponseEntity.ok(updateReservation);
    }
}
