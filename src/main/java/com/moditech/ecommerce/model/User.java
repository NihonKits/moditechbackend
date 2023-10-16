package com.moditech.ecommerce.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String email;

    private String fullName;

    private String password;

    private String userRole = "ROLE_USER";

    private String birthday;

    private Boolean isEnable = false;
}
