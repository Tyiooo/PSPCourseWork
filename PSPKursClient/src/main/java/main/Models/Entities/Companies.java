package main.Models.Entities;

import lombok.*;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Companies {
    private int Id_company;
    private String CompanyName;
    private String CompanyAdress;
    private User User;
    private Set<Researches> researches = new HashSet<>();

}
