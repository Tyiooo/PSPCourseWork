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
@Table(name="researches")
public class Researches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_research",length = 45)
    private int Id_research;

    @Column(name="researchName",length = 45)
    private String ResearchName;

    @Column(name="researchCost",length = 45)
    private int ResearchCost;

    @Column(name="amountOfDays",length = 45)
    private int amountOfDays;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Companies Company;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "Research")
    private Set<Employees> employees = new HashSet<>();
}
