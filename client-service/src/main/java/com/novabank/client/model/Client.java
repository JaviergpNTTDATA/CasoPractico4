package com.novabank.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Firstname is needed")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Lastname is needed")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "DNI is needed")
    @Column(unique = true, nullable = false)
    private String dni;

    @Email
    @NotBlank(message = "Email is needed")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Phone is needed")
    private String phone;

    //@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    //private List<Account> accounts = new ArrayList<>();


    public Client(String firstName, String lastName, String dni, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.phone = phone;
        this.email = email;
    }

}
