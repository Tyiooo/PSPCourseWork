package main.Models.Entities;

import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Researches {

    private int Id_research;

    private String ResearchName;

    private int ResearchCost;

    private int amountOfDays;

    private Companies Company;

    private Set<Employees> employees = new HashSet<>();
}
