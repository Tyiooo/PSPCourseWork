package main.Models.Entities;

import lombok.*;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employees {
    private int Id_employee;

    private String EmployeeName;

    private String EmployeeSurname;

    private int EmployeeSalary;

    private String phoneNumber;

    private Researches Research;
}
