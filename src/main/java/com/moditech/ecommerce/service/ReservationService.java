package com.moditech.ecommerce.service;

import com.moditech.ecommerce.model.Reservation;
import com.moditech.ecommerce.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationList() {
        return reservationRepository.findAll();
    }

    public Reservation updateReservationStatus(String id, Reservation reservation){
        Reservation reservationById = reservationRepository.findById(id).orElse(null);
        assert reservationById != null;
        System.out.println(reservationById.getStatus());
        reservationById.setStatus(reservation.getStatus());
        return reservationRepository.save(reservationById);
    }
}
