package com.novabank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private int accountCount;
    
}
