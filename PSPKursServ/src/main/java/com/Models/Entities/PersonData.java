package com.Models.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="person_data")
public class PersonData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_person_data")
    private int Id;

    @Column(name="age")
    private int Age;

    @Column(name="mail",length = 45)
    private String Mail;

    @Column(name="address",length = 45)
    private String Address;

    @Column(name="sex",length = 45)
    private String Sex;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "PersonData")
    private Set<User> Users = new HashSet<>();
}
