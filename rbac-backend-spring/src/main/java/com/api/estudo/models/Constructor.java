package com.api.estudo.models;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Constructors")
public class Constructor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "constructorid")
    private Integer constructorId;
}
