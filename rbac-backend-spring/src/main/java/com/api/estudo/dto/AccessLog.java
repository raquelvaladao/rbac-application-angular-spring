package com.api.estudo.dto;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "log_table")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userid")
    private Integer userId;

    @Column(name = "datetime")
    private LocalDateTime timestamp;

}
