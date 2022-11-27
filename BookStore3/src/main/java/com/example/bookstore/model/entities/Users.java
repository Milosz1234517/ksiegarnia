package com.example.bookstore.model.entities;

import com.example.bookstore.model.entities.role.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Basic
    @Column(name = "login", nullable = false, length = 20, unique = true)
    private String login;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 15)
    private String surname;

    @Basic
    @Column(name = "phone_number", nullable = false)
    private int phoneNumber;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Basket> basket;

    public Users(String login, String password, String name, String surname, int phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public Users() {

    }
}
