package main.Models.Entities;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private int Id;
    private String Name;
    private String Login;
    private String Password;
    private String Role;
    private Set<Companies> companies = new HashSet<>();
    private PersonData PersonData;
}
