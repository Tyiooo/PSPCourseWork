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
@Table(name="companies")
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_company",length = 45)
    private int Id_company;

    @Column(name="companyName",length = 45)
    private String CompanyName;

    @Column(name="companyAdress",length = 45)
    private String CompanyAdress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User User;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "Company")
    private Set<Researches> researches = new HashSet<>();
}
