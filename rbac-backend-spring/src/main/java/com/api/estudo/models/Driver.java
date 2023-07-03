package com.api.estudo.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driverid")
    private Integer driverId;
}
