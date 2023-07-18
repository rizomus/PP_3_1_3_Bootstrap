package ru.kata.spring.boot_security.demo.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.kata.spring.boot_security.demo.entities.Role;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private int age;

    private String password;

    private String email;

    @ManyToMany()
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Collection<Role> roles;

    public String getFormattedRoles() {
        String fRoles = "";
        for (Role role: this.roles) {
            String r = role.getName();
            fRoles += " " + (r.startsWith("ROLE") ? r.substring(5) : r);
        }
        return fRoles;
    }
}