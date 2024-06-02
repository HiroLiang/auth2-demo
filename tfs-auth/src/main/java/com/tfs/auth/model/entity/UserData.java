package com.tfs.auth.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INT")
    private long id;

    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(name = "NAME", columnDefinition = "NVARCHAR(255)", unique = true)
    private String name;

    @Column(name = "ACTIVATED", columnDefinition = "INT", nullable = false)
    @ColumnDefault("0")
    private Integer activated;

}
