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
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name="name",length = 45)
    private String Name;

    @Column(name="login",length = 45)
    private String Login;

    @Column(name="password",length = 45)
    private String Password;

    @Column(name="role",length = 45)
    private String Role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_person_data")
    private PersonData PersonData;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "User")
    private Set<Companies> companies = new HashSet<>();
}
