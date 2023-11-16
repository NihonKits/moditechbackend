package com.moditech.ecommerce.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "reservation")
public class Reservation {

    @Id
    private String id;

    private String appointmentDate;

    private String appointmentTime;

    private String inquiryMessage;

    private String email;

    private String service;

    private String status = "Pending";

}
