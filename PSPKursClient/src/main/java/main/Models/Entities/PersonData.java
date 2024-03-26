package main.Models.Entities;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonData {

    private int Id;

    private int Age;

    private String Mail;

    private String Address;

    private String Sex;

    private Set<User> Users = new HashSet<>();
}
