package com.Models.Entities;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="employees")
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_employee",length = 45)
    private int Id_employee;

    @Column(name="employeeName",length = 45)
    private String EmployeeName;

    @Column(name="employeeSurname",length = 45)
    private String EmployeeSurname;

    @Column(name="employeeSalary",length = 45)
    private int EmployeeSalary;

    @Column(name="phoneNumber",length = 45)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "research_id")
    private Researches Research;
}
